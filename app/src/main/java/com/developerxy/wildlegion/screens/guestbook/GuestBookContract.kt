package com.developerxy.wildlegion.screens.guestbook

import android.content.Context
import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.screens.guestbook.models.Entry

interface GuestBookContract {
    interface View {
        fun initializeActionBar()
        fun setActionbarTitle(title: String)
        fun setupRecyclerView()
        fun getContext(): Context
        fun onLoadFailed(error: Throwable)
        fun setHeaderStats(stats: String)
        fun showEntries(entries: List<Entry>)
    }

    interface Presenter : BasePresenter {

    }
}