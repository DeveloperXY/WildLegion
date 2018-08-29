package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.screens.main.fragments.members.di.DaggerMembersPresenterComponent
import com.developerxy.wildlegion.screens.main.models.Member
import com.developerxy.wildlegion.screens.main.network.RetrofitModule
import com.developerxy.wildlegion.screens.main.network.WixAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MembersPresenter(var mView: MembersContract.View) : MembersContract.Presenter {

    @Inject
    lateinit var mWixAPI: WixAPI

    var membersList: List<Member> = emptyList()

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
                        onNext = {
                            mView.showMembers(it)
                            membersList = it
                        },
                        onError = mView::showLoadingError,
                        onComplete = mView::hideProgressbar
                )
    }

    override fun onSearchQueryTextChange(newText: String?) {
        mView.showMembers(membersList.filter {
            it.nickname.contains(newText!!, ignoreCase = true)
        })
    }

    override fun showAllMembers() = mView.showMembers(membersList)
}