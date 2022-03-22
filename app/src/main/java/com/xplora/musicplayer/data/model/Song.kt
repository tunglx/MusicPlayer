package com.xplora.musicplayer.data.model

import android.os.Parcelable
import com.xplora.player.model.ASong
import kotlinx.android.parcel.Parcelize

@Suppress("DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES")
@Parcelize
data class Song(
    var id: Long,
    var songName: String?,
    var path: String,
    var artistName: String?,
    var albumArt: String?,
    var duration: String?,
    var type: Int = 0
) : ASong(id, songName, albumArt, artistName, path, type, duration), Parcelable