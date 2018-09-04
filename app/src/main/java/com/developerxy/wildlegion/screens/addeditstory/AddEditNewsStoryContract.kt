package com.developerxy.wildlegion.screens.addeditstory

import android.content.Intent
import com.developerxy.wildlegion.BasePresenter

interface AddEditNewsStoryContract {
    interface View {
        fun initializeActionBar()
        fun setActionbarTitle(title: String)
        fun showErrorMessage(errorMessage: String)
        fun showLoadingView()
        fun hideLoadingView()
        fun setLoadingStatement(text: String)
        fun stopLoadingView()
        fun exit()
        fun exit(resultCode: Int, data: Intent?)
        fun showMissingTitle()
        fun showMissingDate()
        fun showMissingStory()
        fun setTitle(title: String)
        fun setPostDate(postDate: String)
        fun setNewsStory(newsStory: String)
        fun showSaveButton()
        fun hideSaveButton()
    }

    interface Presenter : BasePresenter {
        fun start(intent: Intent)
        fun saveNewsStory(title: String, date: String, newsStory: String)
        fun deleteNews()
        fun goBack()
    }
}