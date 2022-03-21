package com.android.musicplayer.domain.repository

import com.android.musicplayer.data.model.Song

interface PlaylistRepository {
    fun getSongs(): List<Song>?
}