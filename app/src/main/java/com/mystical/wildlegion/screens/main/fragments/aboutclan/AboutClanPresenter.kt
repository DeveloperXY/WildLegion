package com.mystical.wildlegion.screens.main.fragments.aboutclan

import com.mystical.wildlegion.BuildConfig
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class AboutClanPresenter(var mView: AboutClanContract.View) :
        AboutClanContract.Presenter, YouTubePlayer.OnInitializedListener {

    private val WILD_LEGION_ABOUT_US_VIDEO_ID = "ahkLkIwJgtI"
    private var isYoutubeInitialized = false

    override fun initYoutubePlayer() {
        isYoutubeInitialized = true

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .doOnSubscribe {
                    mView.showRulesSection()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView.showRedSection()
                }

        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView.getYoutubeFragment()?.initialize(BuildConfig.YOUTUBE_API_KEY, this)
                }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?,
                                         youtubePlayer: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            mView.showYoutubeContainer()
            mView.hidePlaceholder()
            mView.hideProgressbar()

            youtubePlayer?.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {

                val app = mView.getApp()
                var wasPlayingMusic = app.isBackgroundMusicPlaying()

                override fun onSeekTo(p0: Int) {}

                override fun onBuffering(p0: Boolean) {}

                override fun onPlaying() {
                    if (wasPlayingMusic)
                        app.pauseBackgroundMusic()
                }

                override fun onStopped() {
                    if (wasPlayingMusic)
                        app.playBackgroundMusic()
                }

                override fun onPaused() {
                    if (wasPlayingMusic)
                        app.playBackgroundMusic()
                }
            })
            youtubePlayer?.setOnFullscreenListener {
                if (!it) mView.switchOrientationToPortrait()
            }
            youtubePlayer?.cueVideo(WILD_LEGION_ABOUT_US_VIDEO_ID)
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    override fun detachYoutubePlayer() {
        isYoutubeInitialized = false
    }
}