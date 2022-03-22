package com.xplora.musicplayer.domain.usecase

import com.xplora.musicplayer.data.model.Song
import com.xplora.musicplayer.domain.repository.PlaylistRepository

class GetSongsUseCase(private val playlistRepository: PlaylistRepository) {
    fun getSongs(): List<Song>? {
        return playlistRepository.getSongs()
    }
}