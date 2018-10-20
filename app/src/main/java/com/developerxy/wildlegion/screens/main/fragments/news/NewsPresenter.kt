package com.developerxy.wildlegion.screens.main.fragments.news

import com.developerxy.wildlegion.ApplicationModule
import com.developerxy.wildlegion.data.UserRepository
import com.developerxy.wildlegion.data.di.DaggerUserRepositoryComponent
import com.developerxy.wildlegion.data.di.DatabaseModule
import com.developerxy.wildlegion.di.components.DaggerNewsPresenterComponent
import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.screens.main.models.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsPresenter(var mView: NewsContract.View) : NewsContract.Presenter {

    private var newsList: List<News> = emptyList()

    @Inject
    lateinit var mWixAPI: WixAPI

    @Inject
    lateinit var mUserRepository: UserRepository

    init {
        DaggerUserRepositoryComponent.builder()
                .applicationModule(ApplicationModule(mView.getApplication()))
                .databaseModule(DatabaseModule())
                .build()
                .inject(this)

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
                            mView.showLoadingError(it)
                        },
                        onComplete = mView::stopShimmer
                )
    }

    override fun showAllNews() = mView.showNews(newsList)

    override fun doIfLoggedIn(action: () -> Unit) {
        mUserRepository.isUserLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { status ->
                    if (status) {
                        action()
                    }
                }
    }
}