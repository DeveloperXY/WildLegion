package com.mystical.wildlegion.screens.guestbook

import com.mystical.wildlegion.BasePresenter
import com.mystical.wildlegion.BaseView
import com.mystical.wildlegion.screens.guestbook.models.Entry

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