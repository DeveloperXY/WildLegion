package com.developerxy.wildlegion.screens.main.fragments.aboutclan


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.developerxy.wildlegion.App
import com.developerxy.wildlegion.BuildConfig
import com.developerxy.wildlegion.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_about_clan.*
import java.util.concurrent.TimeUnit


class AboutClanFragment : Fragment(), YouTubePlayer.OnInitializedListener {

    companion object {
        const val WILD_LEGION_ABOUT_US_VIDEO_ID = "ahkLkIwJgtI"
        var isYoutubeInitialized = false
    }

    var youtubePlayerFragment: YouTubePlayerSupportFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_clan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBodyText()
        youtubePlayerFragment = activity?.supportFragmentManager?.findFragmentById(R.id.mYoutubeContainer)
                as YouTubePlayerSupportFragment?
        if (youtubePlayerFragment == null) {
            youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
            childFragmentManager.beginTransaction().add(R.id.mYoutubeContainer, youtubePlayerFragment).commit()
        }

        val progressDrawable = mProgressbar.indeterminateDrawable.mutate()
        progressDrawable.setColorFilter(resources.getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN)
        mProgressbar.indeterminateDrawable = progressDrawable
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?,
                                         youtubePlayer: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            mYoutubeContainer.visibility = VISIBLE
            mPlaceholder.visibility = GONE
            mProgressbar.visibility = GONE

            youtubePlayer?.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {

                val app = (activity?.application) as App
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
                if (!it) {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
            youtubePlayer?.cueVideo(WILD_LEGION_ABOUT_US_VIDEO_ID)
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    private fun setBodyText() {
        mRedSection.text = getString(R.string.about_clan_part_1)
        mRedSection.append("\n\n")
        mRedSection.append(getString(R.string.about_clan_part_2))

        resources.getStringArray(R.array.rules)
                .forEachIndexed { index, rule ->
                    mRulesSection.append("${index + 1}. $rule\n")
                }
    }

    fun initializeYoutubePlayer() {
        isYoutubeInitialized = true

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .doOnSubscribe {
                    mRulesSection.visibility = VISIBLE
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mRedSection.visibility = VISIBLE
                }

        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    youtubePlayerFragment?.initialize(BuildConfig.YOUTUBE_API_KEY, this)
                }
    }
}
