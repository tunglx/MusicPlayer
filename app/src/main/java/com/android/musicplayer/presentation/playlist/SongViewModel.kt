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

    fun getSongById(id: Long): Song? {
        playlistData.value?.let {
            for (song in it) {
                if (song.id == id) {
                    return song
                }
            }
        }
        return null
    }
}