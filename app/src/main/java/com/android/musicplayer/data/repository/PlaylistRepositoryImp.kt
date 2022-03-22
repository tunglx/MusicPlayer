package com.android.musicplayer.data.repository

import android.app.Application
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.repository.PlaylistRepository
import com.android.musicplayer.presentation.songplayer.SongPlayerActivity.Companion.AUDIO_TYPE

class PlaylistRepositoryImp(private val application: Application) : PlaylistRepository {

    override fun getSongs(): List<Song> {
        val mDeviceMusicList = mutableListOf<Song>()
        try {
            val pathColumn =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Audio.AudioColumns.BUCKET_DISPLAY_NAME
                else MediaStore.Audio.AudioColumns.DATA

            val projection = arrayOf(
                MediaStore.Audio.AudioColumns.ARTIST, // 0
                MediaStore.Audio.AudioColumns.YEAR, // 1
                MediaStore.Audio.AudioColumns.TRACK, // 2
                MediaStore.Audio.AudioColumns.TITLE, // 3
                MediaStore.Audio.AudioColumns.DISPLAY_NAME, // 4,
                MediaStore.Audio.AudioColumns.DURATION, //5,
                MediaStore.Audio.AudioColumns.ALBUM, // 6
                MediaStore.Audio.AudioColumns.ALBUM_ID, // 7
                pathColumn, // 8
                MediaStore.Audio.AudioColumns._ID, // 9
                MediaStore.MediaColumns.DATE_MODIFIED // 10
            )

            val selection = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = 1"
            val sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

            val musicCursor = application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
            )

            // Query the storage for music files
            musicCursor?.use { cursor ->

                val artistIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
                val titleIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
                val durationIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
                val albumIdIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)
                val relativePathIndex =
                    cursor.getColumnIndexOrThrow(pathColumn)
                val idIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)

                while (cursor.moveToNext()) {
                    // Now loop through the music files
                    val audioId = cursor.getLong(idIndex)
                    val audioArtist = cursor.getString(artistIndex)
                    val audioTitle = cursor.getString(titleIndex)
                    val audioDuration = cursor.getLong(durationIndex)
                    val albumId = cursor.getString(albumIdIndex)
                    val audioRelativePath = cursor.getString(relativePathIndex)

                    val cursorAlbums = application.contentResolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART),
                        MediaStore.Audio.Albums._ID + "=?",
                        arrayOf<String>(albumId),
                        null
                    )
                    var albumArt: String? = null
                    if (cursorAlbums?.moveToFirst() == true) {
                        albumArt =
                            cursorAlbums.getString(cursorAlbums.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
                    }

                    // Add the current music to the list
                    mDeviceMusicList.add(
                        Song(
                            audioId,
                            audioTitle,
                            audioRelativePath,
                            audioArtist,
                            albumArt,
                            audioDuration.toString(),
                            AUDIO_TYPE
                        )
                    )
                    cursorAlbums?.close()
                }
            }
            musicCursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mDeviceMusicList.sortBy { song -> song.id }
        for (song in mDeviceMusicList) {
            Log.i("tung", "getSongs result: $song")
        }
        return mDeviceMusicList
    }
}