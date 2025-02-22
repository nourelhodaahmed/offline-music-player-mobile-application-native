package com.android.musicplayer.util

import java.util.concurrent.TimeUnit

class PlaySongHelper {

    fun getDurationFormat(duration:Long): String{
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) % 60
            )
    }

}