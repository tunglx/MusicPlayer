package com.xplora.musicplayer.presentation.playlist

import com.xplora.musicplayer.data.model.Song

interface OnPlaylistAdapterListener {
    fun playSong(song: Song, songs: ArrayList<Song>)
}