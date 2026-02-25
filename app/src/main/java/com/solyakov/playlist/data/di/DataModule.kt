package com.solyakov.playlist.data.di

import com.solyakov.playlist.creator.Storage
import com.solyakov.playlist.data.history.SearchHistoryRepositoryImpl
import com.solyakov.playlist.data.network.RetrofitNetworkClient
import com.solyakov.playlist.data.network.TracksRepositoryImpl
import com.solyakov.playlist.data.playlist.PlaylistsRepositoryImpl
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dataModule = module {
    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single {
        Storage()
    }

    single<CoroutineScope>{
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    single {
        SearchHistoryRepositoryImpl(get())

    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl()
    }
}