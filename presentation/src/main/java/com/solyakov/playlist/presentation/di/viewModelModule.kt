package com.solyakov.playlist.presentation.di

import com.solyakov.playlist.presentation.view_model.AddPlaylistScreenViewModel
import com.solyakov.playlist.presentation.view_model.FavoriteTracksViewModel
import com.solyakov.playlist.presentation.view_model.PlaylistsViewModel
import com.solyakov.playlist.presentation.view_model.SearchScreenViewModel
import com.solyakov.playlist.presentation.view_model.TrackViewModel
import com.solyakov.playlist.presentation.view_model.TracksInPlaylistViewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchScreenViewModel(get(), get(), get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        TrackViewModel(get(), get(), get())
    }
    viewModel {
        AddPlaylistScreenViewModel(get(), get())
    }
    viewModel {
        TracksInPlaylistViewModel(get(), get(), get())
    }
    viewModel {
        FavoriteTracksViewModel(get(), get())
    }

}