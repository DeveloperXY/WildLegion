package com.developerxy.wildlegion.screens.main.fragments.members

import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.screens.main.models.Member

interface MembersContract {
    interface View {
        fun setupRecyclerView()
        fun showMembers(members: List<Member>)
    }

    interface Presenter: BasePresenter {

    }
}