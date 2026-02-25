package com.solyakov.playlist.ui.di

import com.solyakov.playlist.ui.view_model.PlaylistsViewModel
import com.solyakov.playlist.ui.view_model.SearchScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchScreenViewModel(get(), get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
}