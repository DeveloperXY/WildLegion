package com.developerxy.wildlegion.screens.guestbook

import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.BaseView
import com.developerxy.wildlegion.screens.guestbook.models.Entry

interface GuestBookContract {
    interface View : BaseView {
        fun initializeActionBar()
        fun setActionbarTitle(title: String)
        fun setupRecyclerView()
        fun onLoadFailed(error: Throwable)
        fun setHeaderStats(stats: String)
        fun showEntries(entries: List<Entry>)
    }

    interface Presenter : BasePresenter
}