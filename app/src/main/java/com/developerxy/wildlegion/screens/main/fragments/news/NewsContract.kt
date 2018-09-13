package com.developerxy.wildlegion.screens.main.fragments.news

import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.screens.main.models.News

interface NewsContract {
    interface View {
        fun setupRecyclerView()
        fun showNews(news: List<News>)
        fun showLoadingError(error: Throwable)
        fun hideLoadingError()
        fun stopRefreshing()
        fun removeNews(position: Int)
        fun showMemberRemovalFailedError()
        fun showShimmer()
        fun stopShimmer()
    }

    interface Presenter : BasePresenter {
        fun loadNews()
        fun showAllNews()
    }
}