package com.developerxy.wildlegion.screens.main.fragments.news

import com.developerxy.wildlegion.di.components.DaggerNewsPresenterComponent
import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.screens.main.models.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsPresenter(var mView: NewsContract.View) : NewsContract.Presenter {

    @Inject
    lateinit var mWixAPI: WixAPI

    private var newsList: List<News> = emptyList()

    init {
        DaggerNewsPresenterComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)
    }

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
                            mView.hideProgressbar()
                            mView.showLoadingError(it)
                        },
                        onComplete = mView::hideProgressbar
                )
    }

    override fun showAllNews() = mView.showNews(newsList)
}