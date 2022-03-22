package com.android.musicplayer.presentation.playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Song
import com.android.player.BaseSongPlayerActivity
import com.android.player.model.ASong
import com.android.player.util.PreferencesHelper
import kotlinx.android.synthetic.main.activity_playlist.*
import org.koin.android.ext.android.inject

class PlaylistActivity : BaseSongPlayerActivity(), OnPlaylistAdapterListener {

    private var adapter: PlaylistAdapter? = null
    private val pref: PreferencesHelper by inject()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.apply {
            if (containsKey(SONG_LIST_KEY)) {
                mSongList = getParcelableArrayList(SONG_LIST_KEY)
                adapter?.songs = mSongList?.toList() as List<Song>

                val latestPlayedSongId = pref.latestPlayedSongId
                mSongList?.let {
                    for (song in it) {
                        if (song.songId == latestPlayedSongId) {
                            adapter?.selectedSong = song as Song
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

        fun start(context: Context, songList: ArrayList<ASong>?) {
            val intent = Intent(context, PlaylistActivity::class.java).apply {
                putExtra(SONG_LIST_KEY, songList)
            }
            context.startActivity(intent)
        }

    }
}
