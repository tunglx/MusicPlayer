package com.xplora.musicplayer.di.module

import android.app.Application
import com.xplora.musicplayer.data.repository.PlaylistRepositoryImp
import com.xplora.musicplayer.domain.repository.PlaylistRepository
import com.xplora.musicplayer.domain.usecase.GetSongsUseCase
import com.xplora.musicplayer.presentation.playlist.SongViewModel
import com.xplora.player.util.PreferencesHelper
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    viewModel { SongViewModel(get()) }

    single { createGetSongsUseCase(get()) }

    single { createPlaylistRepository(get()) }

    single { PreferencesHelper(get()) }
}

fun createGetSongsUseCase(
    playlistRepository: PlaylistRepository
): GetSongsUseCase {
    return GetSongsUseCase(playlistRepository)
}

fun createPlaylistRepository(application: Application): PlaylistRepository {
    return PlaylistRepositoryImp(application)
}