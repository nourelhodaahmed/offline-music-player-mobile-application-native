package com.android.musicplayer.util

import com.android.musicplayer.data.DataManager
import com.android.musicplayer.data.domain.Song

import java.io.File

import kotlin.collections.sortBy

class Extract_files_manually {

    //Manual File System Scan (Much Slower)

    private fun fetch_Songs(f: File): ArrayList<File>
    {
        val result_list: ArrayList<File> = ArrayList<File>()
        val files: Array<out File>? = f.listFiles()

        files?.let {
            for (current_file in files)
            {
                if( (!current_file.isHidden) && current_file.isDirectory )
                {
                    result_list.addAll(fetch_Songs(current_file))
                }
                else if( current_file.name.endsWith(".mp3") && (!current_file.name.startsWith(".")) )
                {
                    result_list.add(current_file)
                }
            }
        }

        result_list.sortBy { it.lastModified() }
        return  result_list
    }

    fun set_Songs_List(f: File){

        val internal_files: ArrayList<File> = fetch_Songs(f)

        internal_files?.let {
            for (file in internal_files){
                DataManager.addSong(Song(file.name,file.absolutePath))
            }
        }

    }
}