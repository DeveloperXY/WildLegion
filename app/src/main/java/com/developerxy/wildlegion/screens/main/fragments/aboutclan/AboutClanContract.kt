package com.developerxy.wildlegion.screens.main.fragments.aboutclan

import com.developerxy.wildlegion.App
import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.BaseView
import com.google.android.youtube.player.YouTubePlayerSupportFragment

interface AboutClanContract {
    interface View: BaseView {
        fun showYoutubeContainer()
        fun hidePlaceholder()
        fun hideProgressbar()
        fun showRedSection()
        fun showRulesSection()
        fun getYoutubeFragment(): YouTubePlayerSupportFragment?
        fun getApp(): App
        fun switchOrientationToPortrait()
    }

    interface Presenter : BasePresenter {
        fun initYoutubePlayer()
        fun detachYoutubePlayer()
    }
}