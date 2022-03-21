package com.android.musicplayer.presentation.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.usecase.GetSongsUseCase

class SongViewModel(
    private val getSongsUseCase: GetSongsUseCase,
) : ViewModel() {

    val playlistData = MutableLiveData<List<Song>>()

    fun getSongs() {
        playlistData.value = getSongsUseCase.getSongs()
    }

    fun getSongByPath(path: String?): Song? {
        playlistData.value?.let {
            path?.let { path ->
                for (song in it) {
                    if (song.path == path) {
                        return song
                    }
                }
            }
        }
        return null
    }
}