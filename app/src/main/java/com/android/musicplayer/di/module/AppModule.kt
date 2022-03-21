package com.android.musicplayer.di.module

import android.app.Application
import com.android.musicplayer.data.repository.PlaylistRepositoryImp
import com.android.musicplayer.domain.repository.PlaylistRepository
import com.android.musicplayer.domain.usecase.GetSongsUseCase
import com.android.musicplayer.presentation.playlist.SongViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    viewModel { SongViewModel(get()) }

    single { createGetSongsUseCase(get()) }

    single { createPlaylistRepository(get()) }
}

fun createGetSongsUseCase(
    playlistRepository: PlaylistRepository
): GetSongsUseCase {
    return GetSongsUseCase(playlistRepository)
}

fun createPlaylistRepository(application: Application): PlaylistRepository {
    return PlaylistRepositoryImp(application)
}