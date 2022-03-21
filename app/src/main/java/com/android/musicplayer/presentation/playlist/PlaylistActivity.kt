package com.android.musicplayer.presentation.playlist

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.presentation.songplayer.SongPlayerActivity
import com.android.player.BaseSongPlayerActivity
import com.android.player.util.PreferencesHelper
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

        viewModel.playlistData.observe(this) {
            adapter?.songs = it
        }

        Log.i("tung", "last played song: ${pref.latestPlayedSongPath}")
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
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
