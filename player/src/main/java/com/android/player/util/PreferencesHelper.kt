package com.android.player.util

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PreferencesHelper(context: Context) {
    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val prefsLatestPlayedSong = "to_restore_song_pref"


    var latestPlayedSongPath: String?
        get() = mPrefs.getString(
            prefsLatestPlayedSong,
            null
        )
        set(value) = mPrefs.edit { putString(prefsLatestPlayedSong, value) }
}