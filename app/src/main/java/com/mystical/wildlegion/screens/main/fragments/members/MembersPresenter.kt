package com.mystical.wildlegion.screens.main.fragments.members

import com.mystical.wildlegion.data.UserRepository
import com.mystical.wildlegion.network.WixAPI
import com.mystical.wildlegion.network.models.DeleteRequest
import com.mystical.wildlegion.screens.main.models.Member
import com.mystical.wildlegion.screens.main.models.MemberHeader
import com.mystical.wildlegion.screens.main.models.MemberItem
import com.mystical.wildlegion.utils.ServiceGenerator
import io.reactivex.Completable
import io.reactivex.Single
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
                .map {
                    membersList = it
                    setupCategorizedList(membersList)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            mView.showMembers(it)
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
        Single.fromCallable {
            val filtered = membersList.filter {
                it.nickname.contains(newText!!, ignoreCase = true)
            }
            setupCategorizedList(filtered)
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    mView.showMembers(list)
                }
    }

    private fun setupCategorizedList(originalList: List<Member>): MutableList<MemberItem> {
        val list = mutableListOf<MemberItem>()
        val academy = originalList.filter { it.rank == 'A' && it.isActive }
        val medium = originalList.filter { it.rank == 'M' && it.isActive }
        val expert = originalList.filter { it.rank == 'E' && it.isActive }
        val inactive = originalList.filter { !it.isActive }
        if (expert.isNotEmpty()) {
            list.add(MemberHeader("EXPERTS"))
            list.addAll(expert)
        }
        if (medium.isNotEmpty()) {
            list.add(MemberHeader("MEDIUMS"))
            list.addAll(medium)
        }
        if (academy.isNotEmpty()) {
            list.add(MemberHeader("ACADEMY"))
            list.addAll(academy)
        }
        if (inactive.isNotEmpty()) {
            list.add(MemberHeader("INACTIVE"))
            list.addAll(inactive)
        }

        return list
    }

    override fun showAllMembers() {
        val list = setupCategorizedList(membersList)
        mView.showMembers(list)
    }

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