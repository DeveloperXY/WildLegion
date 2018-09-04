package com.developerxy.wildlegion.screens.main

import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.screens.main.fragments.members.MembersFragment
import com.developerxy.wildlegion.screens.main.fragments.news.NewsFragment

interface MainContract {
    interface View : MembersFragment.MembersFragmentDelegate,
            NewsFragment.NewsFragmentDelegate {
        fun displayWlLogo()
        fun initializeActionBar()
        fun setupTabLayout()
        fun setFabClickListener()
        fun showFab()
        fun hideFab()
    }

    interface Presenter : BasePresenter {

    }
}