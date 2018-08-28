package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.screens.main.fragments.members.di.DaggerMembersPresenterComponent
import com.developerxy.wildlegion.screens.main.network.RetrofitModule
import com.developerxy.wildlegion.screens.main.network.WixAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MembersPresenter(var mView: MembersContract.View) : MembersContract.Presenter {

    @Inject
    lateinit var mWixAPI: WixAPI

    init {
        DaggerMembersPresenterComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)
    }

    override fun start() {
        mView.setupRecyclerView()

        mWixAPI.getClanMembers()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = mView::showMembers,
                        onError = mView::showLoadingError,
                        onComplete = mView::hideProgressbar
                )
    }
}