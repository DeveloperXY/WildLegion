package com.mystical.wildlegion.screens.main.fragments.recruiting

import com.mystical.wildlegion.BasePresenter

interface RecruitingContract {
    interface View {
        fun displayGuestbook(url: String)
    }

    interface Presenter : BasePresenter
}