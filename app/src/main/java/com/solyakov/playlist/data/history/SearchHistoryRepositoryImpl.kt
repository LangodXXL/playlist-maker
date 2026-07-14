package com.solyakov.playlist.data.history

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.solyakov.playlist.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class SearchHistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): SearchHistoryRepository {


    companion object {
        private val HISTORY_KEY = stringPreferencesKey("search_history_json")
        private const val MAX_HISTORY_SIZE = 10
        private val gson = Gson()
        private val listType = object : TypeToken<List<String>>() {}.type
    }
    override suspend fun addToHistory(word: String) {
        if (word.isBlank()) return

        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                val currentJson = preferences[HISTORY_KEY]
                val currentList = if (currentJson != null) {
                    gson.fromJson<List<String>>(currentJson, listType)
                } else emptyList()
                val newList = mutableListOf(word).apply {
                    addAll(currentList.filter { it != word })
                    if (size > MAX_HISTORY_SIZE) removeAt(lastIndex)
                }
                preferences[HISTORY_KEY] = gson.toJson(newList)
            }
        }
    }

    override fun getHistory(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            val json = preferences[HISTORY_KEY]
            if (json != null) {
                gson.fromJson(json, listType)
            } else emptyList()
        }
    }
}
