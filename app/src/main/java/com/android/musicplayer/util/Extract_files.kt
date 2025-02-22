package com.android.musicplayer.util

import android.content.Context
import android.provider.MediaStore
import com.android.musicplayer.data.DataManager
import com.android.musicplayer.data.domain.Song
import java.io.File

class Extract_files {

    fun getLocalSongs(context: Context) {
        val contentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media.DATA, // File Path
            MediaStore.Audio.Media.DISPLAY_NAME,  // Song Name (without full path)
            MediaStore.Audio.Media.DATE_ADDED     // Date Added
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DATA} LIKE '%.mp3'"
                        // only music files                                //only .mp3

        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC" // Newest first

        val cursor = contentResolver.query(uri, projection, selection, null, sortOrder)

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

            while (it.moveToNext()) {
                val filePath = it.getString(dataColumn)
                val fileName = it.getString(nameColumn)
                val songFile = File(filePath)

                if (songFile.exists()) {
                    DataManager.addSong(Song(fileName.replace(".mp3",""),filePath))
                }

            }
        }
    }

}