package com.android.musicplayer.presentation.playlist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.presentation.songplayer.SongPlayerActivity
import com.android.player.BaseSongPlayerActivity
import com.android.player.util.PreferencesHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class PlaylistActivity : BaseSongPlayerActivity(), OnPlaylistAdapterListener {

    private var adapter: PlaylistAdapter? = null
    private val viewModel: SongViewModel by viewModel()
    private val pref: PreferencesHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        setSupportActionBar(toolbar)

        adapter = PlaylistAdapter(this)
        playlist_recycler_view.adapter = adapter

        viewModel.playlistData.observe(this, Observer {
            adapter?.songs = it
        })

        Log.i("tung", "last played song: ${pref.latestPlayedSongPath}")
    }

    override fun onStart() {
        super.onStart()
        if (isReadPhoneStatePermissionGranted()) {
            viewModel.getSongs()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE
                )
            }
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    private fun isReadPhoneStatePermissionGranted(): Boolean {
        val firstPermissionResult = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return firstPermissionResult == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {// Permission Granted
                    viewModel.getSongs()
                } else {
                    // Permission Denied
                    Snackbar.make(
                        playlist_recycler_view,
                        getString(R.string.you_denied_permission),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun playSong(song: Song, songs: ArrayList<Song>) {
        SongPlayerActivity.start(this, song, songs)
    }


    companion object {

        private val TAG = PlaylistActivity::class.java.name
        const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE = 7031
        const val PICK_AUDIO_KEY = 2017
        const val AUDIO_TYPE = 3
    }
}
