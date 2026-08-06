package com.solyakov.playlist.data.database

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.solyakov.playlist.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl(context: Context): SettingsRepository {
    private val appContext = context.applicationContext
    private val themeKey = booleanPreferencesKey("dark_theme")

    override val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: false
        }

    override suspend fun saveTheme(isDarkTheme: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkTheme
        }
    }

    private companion object {
        val THEME_KEY = booleanPreferencesKey("dark_theme")
    }
}