package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.screens.main.models.Member

interface MembersContract {
    interface View {
        fun setupRecyclerView()
        fun showMembers(members: List<Member>)
        fun showLoadingError(error: Throwable)
        fun hideProgressbar()
    }

    interface Presenter: BasePresenter {

    }
}