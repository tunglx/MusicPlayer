package com.xplora.musicplayer.domain.repository

import com.xplora.musicplayer.data.model.Song

interface PlaylistRepository {
    fun getSongs(): List<Song>?
}