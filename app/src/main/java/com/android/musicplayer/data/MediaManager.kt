package com.android.musicplayer.data

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.android.musicplayer.data.domain.Song
import com.android.musicplayer.util.PlaySongHelper
import java.lang.Exception

object MediaManager {
    private lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper())
    private var helper: PlaySongHelper = PlaySongHelper()

    fun getMediaPlayerDuration(): Int{
        return mediaPlayer.duration
    }

    fun playSong(song: Song, seekBar: SeekBar, currentTimeTextView: TextView, context: Context, bindLayout: ()-> Unit, log: (Any)->Unit){
        if(::mediaPlayer.isInitialized){
            //mediaPlayer.reset()
            mediaPlayer.release()
        }

        mediaPlayer = MediaPlayer.create(context, Uri.parse(song.path))

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            bindLayout()
            updateSeekBar(seekBar, currentTimeTextView)
        }

        mediaPlayer.setOnCompletionListener {
            stopMediaPlayer()
            playSong(DataManager.getNextSong(), seekBar,currentTimeTextView, context, bindLayout, log)
        }
    }

    fun stopMediaPlayer(){
        if(::mediaPlayer.isInitialized){
            if(mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                mediaPlayer.stop()
            }
        }
    }

    fun playNextSongForNotification(context:Context){
        stopMediaPlayer()
        if(::mediaPlayer.isInitialized){
            //mediaPlayer.reset()
            mediaPlayer.release()
        }

        mediaPlayer = MediaPlayer.create(context, Uri.parse(DataManager.getNextSong().path))

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }

        mediaPlayer.setOnCompletionListener {
            stopMediaPlayer()
            playNextSongForNotification(context)
        }
    }

    fun playPrevSongForNotification(context:Context){
        stopMediaPlayer()
        if(::mediaPlayer.isInitialized){
            //mediaPlayer.reset()
            mediaPlayer.release()
        }

        mediaPlayer = MediaPlayer.create(context, Uri.parse(DataManager.getPreviousSong().path))

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }

        mediaPlayer.setOnCompletionListener {
            stopMediaPlayer()
            playPrevSongForNotification(context)
        }


    }

    fun isSongFinished():Boolean{
        return (mediaPlayer.currentPosition >= (mediaPlayer.duration - 1000))
    }

    fun isthereSongPlaying():Boolean{
        return (::mediaPlayer.isInitialized && mediaPlayer.isPlaying)
    }

    fun isMediaInitialized():Boolean{
        return (::mediaPlayer.isInitialized)
    }

    fun pauseSong(){
        mediaPlayer.pause()
    }

    fun startSong(){
        mediaPlayer.start()
    }

    fun seekToProgress(progress:Int){
        mediaPlayer.seekTo(progress)
    }

    fun updateSeekBar(seekBar: SeekBar, currentTimeTextView: TextView) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    seekBar.progress = mediaPlayer.currentPosition
                    currentTimeTextView.text = helper.getDurationFormat(mediaPlayer.currentPosition.toLong())
                    handler.postDelayed(this, 1000) // Update every second
                }
            }
        }, 0)
    }


}