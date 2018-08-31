package com.developerxy.wildlegion.screens.main.fragments.members

import android.support.v7.widget.SearchView
import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.screens.main.models.Member

interface MembersContract {
    interface View : SearchView.OnQueryTextListener {
        fun setupRecyclerView()
        fun showMembers(members: List<Member>)
        fun showLoadingError(error: Throwable)
        fun hideLoadingError()
        fun showProgressbar()
        fun hideProgressbar()
        fun stopRefreshing()
    }

    interface Presenter : BasePresenter {
        fun onSearchQueryTextChange(newText: String?)
        fun loadClanMembers()
        fun showAllMembers()
    }
}