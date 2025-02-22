package com.android.musicplayer.data

import com.android.musicplayer.data.domain.Song

object DataManager {
    private var songsList: ArrayList<Song> = ArrayList<Song>()
    private var songsnames: ArrayList<String> = ArrayList<String>()
    private var playing_song_index: Int = 0
    private var flag_permission: Boolean = false
    val songs: List<Song>
        get() = songsList.toList()

    fun addSong(song: Song){
        if (!songsList.contains(song))
        {
            songsList.add(song)
            songsnames.add(song.name)
            //no need for replace
            //as MediaStore.Audio.Media.DISPLAY_NAME does not have .mp3
            //songsnames.add(song.name.replace(".mp3",""))
        }
    }


    fun SongsListIsNullOrEmpty():Boolean{
        return songsList.isNullOrEmpty()
    }

    fun permissionGranted(){
        flag_permission = true
    }

    fun isPermissionGranted():Boolean{
        return flag_permission
    }

    fun isContainedItems():Boolean{
        return (songsList.size != 0)
    }

    fun setPlayingSongIndex(index: Int){
        playing_song_index = index
    }


    fun getCurrentSong():Song{
        return songsList[playing_song_index]
    }

    fun getCurrentSongName():String{
        return songsList[playing_song_index].name
    }

    fun getPreviousSong():Song{
        playing_song_index--
        if (playing_song_index == -1)
        {
            playing_song_index = songsList.size - 1
        }
        return songsList[playing_song_index]
    }

    fun getNextSong():Song{
        playing_song_index++
        if (playing_song_index == songsList.size)
        {
            playing_song_index = 0
        }
        return songsList[playing_song_index]
    }

}