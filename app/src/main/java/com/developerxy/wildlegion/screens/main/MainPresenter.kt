package com.developerxy.wildlegion.screens.main

import android.widget.Toast
import com.developerxy.wildlegion.data.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(var mView: MainContract.View) : MainContract.Presenter {

    private var mUserRepository = UserRepository.getInstance(mView.getContext())

    override fun getUserRepository() = mUserRepository

    override fun start() {
        mView.displayWlLogo()
        mView.initializeActionBar()
        mView.setupTabLayout()
        mView.addViewPagerChangeListener()

        mUserRepository.isUserLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { status ->
                    if (status) {
                        Toast.makeText(mView.getContext(), "LOGIN SUCCESS", Toast.LENGTH_LONG).show()
                        mView.setFabClickListener()
                        mView.showFab()
                    } else {
                        Toast.makeText(mView.getContext(), "LOGIN FAILURE", Toast.LENGTH_LONG).show()
                    }
                }
    }
}