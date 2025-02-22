package com.android.musicplayer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>: AppCompatActivity() {

    abstract val LOG_TAG: String
    abstract val bindingInflater: (LayoutInflater)-> T
    private var _binding: ViewBinding? = null
    protected val binding
        get() = _binding as T?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = bindingInflater(layoutInflater)
        setContentView(requireNotNull(_binding).root)

        setup()
        callbacks()

    }

    abstract fun setup()

    abstract fun callbacks()

    protected fun log(msg: Any){
        Log.d(LOG_TAG,msg.toString())
    }

}