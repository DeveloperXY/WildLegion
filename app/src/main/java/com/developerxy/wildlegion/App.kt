package com.developerxy.wildlegion

import android.app.Application
import android.media.MediaPlayer
import android.support.v7.app.AppCompatDelegate

class App : Application(), LifeCycleDelegate {

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)

        mediaPlayer = MediaPlayer.create(this, R.raw.wl_bg_music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onAppBackgrounded() {
        mediaPlayer.pause()
    }

    override fun onAppForegrounded() {
        mediaPlayer.start()
    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }
}