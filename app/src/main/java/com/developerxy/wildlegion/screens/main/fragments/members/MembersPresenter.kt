package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.network.RetrofitModule
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.network.models.RemoveClanMemberRequest
import com.developerxy.wildlegion.screens.main.models.Member
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
        loadClanMembers()
    }

    override fun loadClanMembers() {
        mView.hideLoadingError()
        mView.stopRefreshing()

        mWixAPI.getClanMembers()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            mView.showMembers(it)
                            membersList = it
                        },
                        onError = {
                            mView.showMembers(arrayListOf())
                            mView.hideProgressbar()
                            mView.showLoadingError(it)
                        },
                        onComplete = mView::hideProgressbar
                )
    }

    override fun onSearchQueryTextChange(newText: String?) {
        mView.showMembers(membersList.filter {
            it.nickname.contains(newText!!, ignoreCase = true)
        })
    }

    override fun showAllMembers() = mView.showMembers(membersList)

    override fun removeClanMember(member: Member, position: Int) {
        mWixAPI.removeClanMember(RemoveClanMemberRequest(member._id))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            mView.removeMember(position)
                            mView.showMemberRemovedMessage(member.nickname)
                        },
                        onError = { mView.showMemberRemovalFailedError() }
                )
    }
}