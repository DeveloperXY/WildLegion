package com.developerxy.wildlegion.screens.main.fragments.recruiting

import com.developerxy.wildlegion.BasePresenter

interface RecruitingContract {
    interface View {
        fun setRecruitmentDescription(text: String)
        fun showRecruitmentDescription()
        fun hideProgressbar()
        fun showErrorMessage()
        fun hideGuestbookButton()
        fun showGuestbookButton()
    }

    interface Presenter : BasePresenter {

    }
}