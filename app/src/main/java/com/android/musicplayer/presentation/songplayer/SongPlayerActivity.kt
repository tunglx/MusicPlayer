package com.android.musicplayer.presentation.songplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.load
import coil.request.CachePolicy
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.presentation.playlist.PlaylistActivity
import com.android.musicplayer.presentation.playlist.SongViewModel
import com.android.player.BaseSongPlayerActivity
import com.android.player.model.ASong
import com.android.player.util.OnSwipeTouchListener
import com.android.player.util.PreferencesHelper
import com.android.player.util.formatTimeInMillisToString
import kotlinx.android.synthetic.main.activity_song_player.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class SongPlayerActivity : BaseSongPlayerActivity() {

    private val songViewModel: SongViewModel by viewModel()
    private val pref: PreferencesHelper by inject()

    private fun initSong(song: Song, songList: List<Song>) {
        mSongList = songList.toMutableList()
        mSong = song

        mSongList?.let {
            mSong?.let { it1 ->
                play(it, it1)
                loadInitialData(it1)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_player)

        getSongs()
        songViewModel.playlistData.observe(this) {
            songViewModel.getSongById(pref.latestPlayedSongId)?.let { lastPlayedSong ->
                initSong(lastPlayedSong, it)
            } ?: initSong(it[0], it)
        }

        with(songPlayerViewModel) {

            songDurationData.observe(this@SongPlayerActivity, Observer {
                song_player_progress_seek_bar.max = it
            })

            songPositionTextData.observe(
                this@SongPlayerActivity
            ) { t -> song_player_passed_time_text_view.text = t }

            songPositionData.observe(this@SongPlayerActivity) {
                song_player_progress_seek_bar.progress = it
            }

            isRepeatData.observe(this@SongPlayerActivity) {
                song_player_repeat_image_view.setImageResource(
                    if (it) R.drawable.ic_repeat_one_color_primary_vector
                    else R.drawable.ic_repeat_one_black_vector
                )
            }

            isShuffleData.observe(this@SongPlayerActivity) {
                song_player_shuffle_image_view.setImageResource(
                    if (it) R.drawable.ic_shuffle_color_primary_vector
                    else R.drawable.ic_shuffle_black_vector
                )
            }

            isPlayData.observe(this@SongPlayerActivity) {
                song_player_toggle_image_view.setImageResource(if (it) R.drawable.ic_pause_vector else R.drawable.ic_play_vector)
            }

            playerData.observe(this@SongPlayerActivity) {
                loadInitialData(it)
            }
        }

        song_player_container.setOnTouchListener(object :
            OnSwipeTouchListener(this@SongPlayerActivity) {
            override fun onSwipeRight() {
                if (mSongList?.size ?: 0 > 1) previous()

            }

            override fun onSwipeLeft() {
                if (mSongList?.size ?: 0 > 1) next()
            }
        })

        song_player_progress_seek_bar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) seekTo(p1.toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //Nothing to do here
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //Nothing to do here
            }

        })

        song_player_skip_next_image_view.setOnClickListener {
            next()
        }

        song_player_skip_back_image_view.setOnClickListener {
            previous()
        }

        song_player_toggle_image_view.setOnClickListener {
            toggle()
        }

        song_player_shuffle_image_view.setOnClickListener {
            shuffle()
        }

        song_player_repeat_image_view.setOnClickListener {
            repeat()
        }

        btn_song_list.setOnClickListener {
            PlaylistActivity.start(this, songViewModel.playlistData.value as ArrayList<Song>?)
        }
    }

    private fun loadInitialData(aSong: ASong) {
        song_player_title_text_view.text = aSong.title
        song_player_singer_name_text_view.text = aSong.artist
        song_player_total_time_text_view.text =
            formatTimeInMillisToString(aSong.length?.toLong() ?: 0L)

        if (aSong.clipArt.isNullOrEmpty()) song_player_image_view.setImageResource(R.drawable.placeholder)
        aSong.clipArt?.let {
            song_player_image_view.load(File(it)) {
                CachePolicy.ENABLED
            }
        }
    }

    private fun getSongs() {
        if (isReadPhoneStatePermissionGranted()) {
            songViewModel.getSongs()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE
                )
            }
        }
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
                    songViewModel.getSongs()
                } else {
                    // Permission Denied
                    Toast.makeText(
                        this,
                        getString(R.string.you_denied_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private val TAG = SongPlayerActivity::class.java.name
        const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE = 7031
        const val PICK_AUDIO_KEY = 2017
        const val AUDIO_TYPE = 3
    }
}