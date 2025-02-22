package com.android.musicplayer.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

import android.content.Intent
import android.content.pm.PackageManager

import android.net.Uri
import android.os.Build

import android.provider.Settings

import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.musicplayer.R
import com.android.musicplayer.Services.NotificationService

import com.android.musicplayer.data.DataManager
import com.android.musicplayer.data.MediaManager
import com.android.musicplayer.databinding.ActivityMainBinding
import com.android.musicplayer.util.Constants
import com.android.musicplayer.util.Extract_files

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : BaseActivity<ActivityMainBinding>(), SongListInteractionListener {
    override val LOG_TAG: String = Constants.LOG_TAG_NAMES.MainActivity_LOG_TAG

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    var extractFiles: Extract_files = Extract_files()

    override fun setup() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        setupFloatingButton()
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener
            {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?)
                {
                    DataManager.permissionGranted()
                    extractFiles.getLocalSongs(this@MainActivity)
                    //extractFiles.set_Songs_List(Environment.getExternalStorageDirectory())

                    val adaper = SongAdapter(DataManager.songs,this@MainActivity)
                    binding?.apply {
                        songsListView.adapter = adaper
                    }
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (p0?.isPermanentlyDenied == true) {
                        // User checked "Don't ask again" - send them to settings
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", packageName, null)
                        startActivity(intent)
                    }
                    finish()
                    //to close the app if denied
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                    // Keep requesting until the user explicitly denies
                }

            }).check()
    }
    override fun callbacks() {

//        val intent = Intent(this, NotificationService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            startForegroundService(intent)
//        }
//

        if(!DataManager.isPermissionGranted())
        {
            binding?.apply {
                suggestText.text = "Accept READ_EXTERNAL_STORAGE permission please"
                songsListView.visibility = View.GONE
            }
        }
        else if(!DataManager.isContainedItems())
        {
            binding?.apply {
                suggestText.text = "No Songs Found! please save songs"
                songsListView.visibility = View.GONE
            }
        }
        else{
                binding?.floatingButton!!.setOnClickListener {
                    if(MediaManager.isthereSongPlaying()){
                        MediaManager.pauseSong()
                        bindFloatingButton(R.drawable.play_song_,"Play")
                    }else{
                        MediaManager.startSong()
                        bindFloatingButton(R.drawable.pause_song_,"Stop")
                    }

                }
            }

        }


    private fun setupFloatingButton() {
        if (MediaManager.isMediaInitialized()) {
            if (MediaManager.isthereSongPlaying()) {
                binding?.floatingButton!!.apply {
                    visibility = View.VISIBLE
                    bindFloatingButton(R.drawable.pause_song_, "Stop")
                }
            } else {
                binding?.floatingButton!!.apply {
                    visibility = View.VISIBLE
                    bindFloatingButton(R.drawable.play_song_, "Play")
                }
            }
        }
    }

    private fun bindFloatingButton(id: Int, des: String) {
        val img = ContextCompat.getDrawable(this@MainActivity, id)
        binding?.floatingButton!!.setImageDrawable(img)
        binding?.floatingButton!!.contentDescription = des
    }


    override fun onClickItem(position: Int) {
        DataManager.setPlayingSongIndex(position)
        val intent = Intent(this@MainActivity, PlaySong::class.java)
        startActivity(intent)
    }

    /*override fun onStop() {
        super.onStop()
        val intent = Intent(this, NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(intent)
        }
         // Start service when app is minimized or closed
    }*/

    /*override fun onResume() {
        super.onResume()
        val intent = Intent(this, NotificationService::class.java)
        stopService(intent) // Stop service when app is open
    }*/



}