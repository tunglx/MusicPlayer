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
}