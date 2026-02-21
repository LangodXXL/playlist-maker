package com.solyakov.playlist.data.history

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun addToHistory(word: String)

    fun getHistory(): Flow<List<String>>
}


