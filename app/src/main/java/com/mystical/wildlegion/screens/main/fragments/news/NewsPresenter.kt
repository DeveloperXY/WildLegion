package com.mystical.wildlegion.screens.main.fragments.news

import com.mystical.wildlegion.data.UserRepository
import com.mystical.wildlegion.network.WixAPI
import com.mystical.wildlegion.screens.main.models.News
import com.mystical.wildlegion.utils.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class NewsPresenter(var mView: NewsContract.View) : NewsContract.Presenter {

    private var newsList: List<News> = emptyList()

    private var mWixAPI = ServiceGenerator.createService(WixAPI::class.java)
    private var mUserRepository = UserRepository.getInstance(mView.getContext())

    override fun getUserRepository() = mUserRepository

    override fun start() {
        mView.setupRecyclerView()
        loadNews()
    }

    override fun loadNews() {
        mView.hideLoadingError()
        mView.stopRefreshing()

        mWixAPI.getNews()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            mView.showNews(it)
                            newsList = it
                        },
                        onError = {
                            mView.showNews(arrayListOf())
                            mView.showLoadingError(it)
                        },
                        onComplete = mView::stopShimmer
                )
    }

    override fun showAllNews() = mView.showNews(newsList)
}