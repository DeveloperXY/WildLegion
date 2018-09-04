package com.developerxy.wildlegion.screens.main.fragments.news

import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.screens.main.models.News

interface NewsContract {
    interface View {
        fun setupRecyclerView()
        fun showNews(news: List<News>)
        fun showLoadingError(error: Throwable)
        fun hideLoadingError()
        fun showProgressbar()
        fun hideProgressbar()
        fun stopRefreshing()
        fun removeMember(position: Int)
        fun showMemberRemovalFailedError()
        fun showMemberRemovedMessage(memberName: String)
        fun revertItemSwipe(position: Int)
    }

    interface Presenter : BasePresenter {
        fun loadNews()
        fun showAllNews()
    }
}