package com.solyakov.playlist.data.di

import com.solyakov.playlist.creator.Storage
import com.solyakov.playlist.data.network.RetrofitNetworkClient
import com.solyakov.playlist.data.network.TracksRepositoryImpl
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.domain.api.TracksRepository
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
}