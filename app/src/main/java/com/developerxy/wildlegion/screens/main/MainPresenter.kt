package com.developerxy.wildlegion.screens.main

import android.widget.Toast
import com.developerxy.wildlegion.ApplicationModule
import com.developerxy.wildlegion.data.UserRepository
import com.developerxy.wildlegion.data.di.DaggerUserRepositoryComponent
import com.developerxy.wildlegion.data.di.DatabaseModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter(var mView: MainContract.View): MainContract.Presenter {

    @Inject
    lateinit var mUserRepository: UserRepository

    init {
        DaggerUserRepositoryComponent.builder()
                .applicationModule(ApplicationModule(mView.getApplication()))
                .databaseModule(DatabaseModule())
                .build()
                .inject(this)
    }

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