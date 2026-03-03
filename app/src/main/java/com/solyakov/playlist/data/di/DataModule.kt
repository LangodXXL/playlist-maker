package com.solyakov.playlist.data.di


import com.solyakov.playlist.data.history.SearchHistoryRepositoryImpl
import com.solyakov.playlist.data.network.ITunesApiService
import com.solyakov.playlist.data.network.RetrofitNetworkClient
import com.solyakov.playlist.data.network.TracksRepositoryImpl
import com.solyakov.playlist.data.playlist.PlaylistsRepositoryImpl
import com.solyakov.playlist.domain.api.NetworkClient
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    val baseUrl = "https://itunes.apple.com"


    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<ITunesApiService> {
        get<Retrofit>().create(ITunesApiService::class.java)
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
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