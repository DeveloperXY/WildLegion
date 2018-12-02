package com.mystical.wildlegion.screens.main.fragments.news

import android.app.Application
import com.mystical.wildlegion.BasePresenter
import com.mystical.wildlegion.BaseView
import com.mystical.wildlegion.screens.main.models.News

interface NewsContract {
    interface View: BaseView {
        fun setupRecyclerView()
        fun showNews(news: List<News>)
        fun showLoadingError(error: Throwable)
        fun hideLoadingError()
        fun stopRefreshing()
        fun removeNews(position: Int)
        fun showMemberRemovalFailedError()
        fun showShimmer()
        fun stopShimmer()
        fun getApplication(): Application
    }

    interface Presenter : BasePresenter {
        fun loadNews()
        fun showAllNews()
    }
}