package com.mystical.wildlegion

import android.app.Application
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.mystical.wildlegion.data.UserRepository


class App : Application(), LifeCycleDelegate {

    private lateinit var mUserRepository: UserRepository
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mSharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        mUserRepository = UserRepository.getInstance(this)

        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val shouldPlayMusic = sharedPreferences.getBoolean(getString(R.string.pref_key_play_music), true)
        mediaPlayer = MediaPlayer.create(this, R.raw.wl_bg_music)
        mediaPlayer.isLooping = true

        if (shouldPlayMusic)
            mediaPlayer.start()

        mSharedPrefListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPref, key ->
            if (key == getString(R.string.pref_key_play_music)) {
                val play = sharedPref.getBoolean(getString(R.string.pref_key_play_music), true)
                if (play)
                    mediaPlayer.start()
                else
                    mediaPlayer.pause()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPrefListener)

        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener {
                    if (!it.isSuccessful)
                        Toast.makeText(applicationContext, "SUBSCRIPTION FAILED.", Toast.LENGTH_LONG).show()
                }
    }

    override fun onAppBackgrounded() {
        mediaPlayer.pause()
    }

    override fun onAppForegrounded() {
        val shouldPlayMusic = sharedPreferences.getBoolean(getString(R.string.pref_key_play_music), true)
        if (shouldPlayMusic)
            mediaPlayer.start()
    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }

    fun isBackgroundMusicPlaying() = mediaPlayer.isPlaying

    fun playBackgroundMusic() {
        if (!isBackgroundMusicPlaying())
            mediaPlayer.start()
    }

    fun pauseBackgroundMusic() {
        if (isBackgroundMusicPlaying())
            mediaPlayer.pause()
    }

    fun isUserLoggedIn() = mUserRepository.isUserLoggedIn()
}