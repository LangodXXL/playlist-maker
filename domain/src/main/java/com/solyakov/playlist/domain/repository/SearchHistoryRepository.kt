package com.solyakov.playlist.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    suspend fun addToHistory(word: String)

    fun getHistory(): Flow<List<String>>
}