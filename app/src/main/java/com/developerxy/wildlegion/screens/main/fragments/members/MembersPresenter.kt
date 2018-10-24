package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.data.UserRepository
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.network.models.DeleteRequest
import com.developerxy.wildlegion.screens.main.models.Member
import com.developerxy.wildlegion.utils.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MembersPresenter(var mView: MembersContract.View) : MembersContract.Presenter {

    private var mWixAPI = ServiceGenerator.createService(WixAPI::class.java)

    private var membersList: List<Member> = emptyList()
    private var mUserRepository = UserRepository.getInstance(mView.getContext())

    override fun getUserRepository() = mUserRepository

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
        mWixAPI.removeClanMember(DeleteRequest(member._id))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            mView.removeMember(position)
                        },
                        onError = {
                            mView.showMemberRemovalFailedError()
                            mView.revertItemSwipe(position)
                        }
                )
    }
}