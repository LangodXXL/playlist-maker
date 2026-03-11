package com.solyakov.playlist.ui.di

import com.solyakov.playlist.ui.view_model.AddPlaylistScreenViewModel
import com.solyakov.playlist.ui.view_model.FavoriteTracksViewModel
import com.solyakov.playlist.ui.view_model.PlaylistsViewModel
import com.solyakov.playlist.ui.view_model.SearchScreenViewModel
import com.solyakov.playlist.ui.view_model.TracksInPlaylistViewModel
import com.solyakov.playlist.ui.view_model.TrackViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchScreenViewModel(get(), get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        TrackViewModel(get(), get())
    }
    viewModel {
        AddPlaylistScreenViewModel(get(), get())
    }
    viewModel {
        TracksInPlaylistViewModel(get())
    }
    viewModel {
        FavoriteTracksViewModel(get())
    }

}