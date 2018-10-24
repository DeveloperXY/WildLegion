package com.developerxy.wildlegion.screens.main.fragments.members

import android.support.v7.widget.SearchView
import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.BaseView
import com.developerxy.wildlegion.screens.main.models.Member

interface MembersContract {
    interface View : BaseView, SearchView.OnQueryTextListener {
        fun setupRecyclerView()
        fun showMembers(members: List<Member>)
        fun showLoadingError(error: Throwable)
        fun hideLoadingError()
        fun showProgressbar()
        fun hideProgressbar()
        fun stopRefreshing()
        fun removeMember(position: Int)
        fun showMemberRemovalFailedError()
        fun revertItemSwipe(position: Int)
    }

    interface Presenter : BasePresenter {
        fun onSearchQueryTextChange(newText: String?)
        fun loadClanMembers()
        fun showAllMembers()
        fun removeClanMember(member: Member, position: Int)
    }
}