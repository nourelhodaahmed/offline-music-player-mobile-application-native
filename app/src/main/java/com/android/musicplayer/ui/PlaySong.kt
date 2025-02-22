package com.android.musicplayer.ui

import android.content.Intent
import android.view.LayoutInflater
import android.widget.SeekBar
import com.android.musicplayer.R
import com.android.musicplayer.data.DataManager
import com.android.musicplayer.data.MediaManager
import com.android.musicplayer.databinding.ActivityPlaySongBinding
import com.android.musicplayer.util.Constants
import com.android.musicplayer.util.PlaySongHelper

class PlaySong : BaseActivity<ActivityPlaySongBinding>() {
    override val LOG_TAG: String = Constants.LOG_TAG_NAMES.PlaySongActivity_LOG_TAG
    override val bindingInflater: (LayoutInflater) -> ActivityPlaySongBinding = ActivityPlaySongBinding::inflate

    var helper: PlaySongHelper = PlaySongHelper()

    override fun setup() {
        MediaManager.playSong(
            DataManager.getCurrentSong(),
            binding?.seekBar!!,
            binding?.currentDuration!!,
            this,
            ::bindLayout,
            ::log)
    }

    override fun callbacks() {

        binding?.apply {
            backBotton.setOnClickListener {
                //MediaManager.stopMediaPlayer(::log)
                val intent = Intent(this@PlaySong,MainActivity::class.java)
                startActivity(intent)
                //finish()
            }

            pausePlay.setOnClickListener{
                MediaManager.apply {
                    if(isthereSongPlaying()){
                        pausePlay.setImageResource(R.drawable.play_song_)
                        pauseSong()
                    }
                    else{
                        pausePlay.setImageResource(R.drawable.pause_song_)
                        startSong()
                    }
                }
            }

            previousSong.setOnClickListener {
                MediaManager.apply {
                    stopMediaPlayer()
                    playSong(
                        DataManager.getPreviousSong(),
                        binding?.seekBar!!,
                        binding?.currentDuration!!,
                        this@PlaySong,
                        ::bindLayout,
                        ::log)
                }
            }

            nextSong.setOnClickListener {
                MediaManager.apply {
                    stopMediaPlayer()
                    playSong(
                        DataManager.getNextSong(),
                        binding?.seekBar!!,
                        binding?.currentDuration!!,
                        this@PlaySong,
                        ::bindLayout,
                        ::log)
                }
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    //onProgressChanged doesn't guarantee that it runs exactly at the end of the song
                    /*MediaManager.apply {
                        if(isSongFinished()){
                            stopMediaPlayer(::log)
                            playSong(DataManager.getNextSong(), this@PlaySong, ::bindLayout, ::log)
                        }
                    }*/
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                    binding?.apply {
                        currentDuration.text = helper.getDurationFormat(p0?.progress!!.toLong())
                    }
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    if (p0 != null) {
                        MediaManager.seekToProgress(p0.progress)
                    }
                }

            })
        }

    }

    fun bindLayout(){
        binding?.apply {
            songNameTxt.text = DataManager.getCurrentSongName()
            seekBar.max = MediaManager.getMediaPlayerDuration()
            seekBar.progress = 0
            pausePlay.setImageResource(R.drawable.pause_song_)
            totalDuration.text = helper.getDurationFormat(MediaManager.getMediaPlayerDuration().toLong())
        }
    }

}