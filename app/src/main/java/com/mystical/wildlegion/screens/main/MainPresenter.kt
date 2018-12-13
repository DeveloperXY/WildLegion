package com.mystical.wildlegion.screens.main

import com.mystical.wildlegion.data.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class MainPresenter(var mView: MainContract.View) : MainContract.Presenter {

    private var mUserRepository = UserRepository.getInstance(mView.getContext())

    override fun getUserRepository() = mUserRepository

    override fun start() {
        mView.displayWlLogo()
        mView.initializeActionBar()
        mView.setupTabLayout()
        mView.addViewPagerChangeListener()
        mView.setFabClickListener()
        mView.setupNavigationView()

        updateNavigationHeaderInfo()
    }

    override fun updateNavigationHeaderInfo() {
        doIf(
                ifLoggedIn = {
                    mView.showFab()
                    mUserRepository.getCurrentUser()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(onSuccess = {
                                mView.displayCurrentUsername(it.nickname)
                                mView.displayCurrentUserEmail(it.email)
                            },
                                    onComplete = {
                                        mView.displayCurrentUsername("You're not logged in.")
                                    })
                },
                ifLoggedOut = {
                    mView.displayCurrentUsername("You're not logged in.")
                }
        )
    }
}