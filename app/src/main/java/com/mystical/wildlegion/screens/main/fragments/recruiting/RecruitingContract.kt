package com.mystical.wildlegion.screens.main.fragments.recruiting

import com.mystical.wildlegion.BasePresenter

interface RecruitingContract {
    interface View {
        fun setRecruitmentDescription(text: String)
        fun showRecruitmentDescription()
        fun hideProgressbar()
        fun showErrorMessage()
        fun hideGuestbookButton()
        fun showGuestbookButton()
        fun setListenerOnGuestBookButton()
    }

    interface Presenter : BasePresenter {

    }
}