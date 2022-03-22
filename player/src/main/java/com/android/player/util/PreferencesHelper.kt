package com.android.player.util

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PreferencesHelper(context: Context) {
    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val prefsLatestPlayedSong = "to_restore_song_pref"


    var latestPlayedSongId: Long
        get() = mPrefs.getLong(
            prefsLatestPlayedSong,
            0L
        )
        set(value) = mPrefs.edit { putLong(prefsLatestPlayedSong, value) }
}