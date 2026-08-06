package com.solyakov.playlist.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkTheme: Flow<Boolean>

    suspend fun saveTheme(isDarkTheme: Boolean)
}