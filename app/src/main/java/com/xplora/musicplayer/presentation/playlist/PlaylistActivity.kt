package com.xplora.musicplayer.presentation.playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.xplora.musicplayer.R
import com.xplora.musicplayer.data.model.Song
import com.xplora.player.BaseSongPlayerActivity
import com.xplora.player.util.PreferencesHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_playlist.*
import org.koin.android.ext.android.inject
import java.lang.reflect.Type


class PlaylistActivity : BaseSongPlayerActivity(), OnPlaylistAdapterListener {

    private var adapter: PlaylistAdapter? = null
    private val pref: PreferencesHelper by inject()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.apply {
            if (containsKey(SONG_LIST_KEY)) {
                val type: Type = object : TypeToken<List<Song?>?>() {}.type
                val songList =
                    Gson().fromJson(intent.getStringExtra(SONG_LIST_KEY), type) as List<Song>
                adapter?.songs = songList

                val latestPlayedSongId = pref.latestPlayedSongId
                songList.let {
                    for (song in it) {
                        if (song.songId == latestPlayedSongId) {
                            adapter?.selectedSong = song
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        adapter = PlaylistAdapter(this)
        playlist_recycler_view.adapter = adapter
        playlist_recycler_view.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        onNewIntent(intent)
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun playSong(song: Song, songs: ArrayList<Song>) {
        play(songs.toMutableList(), song)
        adapter?.selectedSong = song
    }


    companion object {

        private val TAG = PlaylistActivity::class.java.name

        fun start(context: Context, songList: ArrayList<Song>?) {
            val intent = Intent(context, PlaylistActivity::class.java).apply {
                putExtra(SONG_LIST_KEY, Gson().toJson(songList))
            }
            context.startActivity(intent)
        }

    }
}
