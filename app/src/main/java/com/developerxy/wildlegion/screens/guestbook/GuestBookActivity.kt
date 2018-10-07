package com.developerxy.wildlegion.screens.guestbook

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.BackgroundActivity
import com.developerxy.wildlegion.screens.guestbook.models.Entry
import com.developerxy.wildlegion.utils.SpacesItemDecoration
import com.developerxy.wildlegion.utils.dpToPx
import kotlinx.android.synthetic.main.activity_guest_book.*

class GuestBookActivity : BackgroundActivity(), GuestBookContract.View {

    lateinit var mPresenter: GuestBookPresenter
    lateinit var mEntryAdapter: EntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_book)

        mPresenter = GuestBookPresenter(this)
        mPresenter.start()
    }

    override fun initializeActionBar() {
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.isTitleEnabled = false
    }

    override fun setActionbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun getContext() = this

    override fun onLoadFailed(error: Throwable) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }

    override fun setHeaderStats(stats: String) {
        statsHeader.text = stats
    }

    override fun setupRecyclerView() {
        mEntryAdapter = EntryAdapter(this, mutableListOf())
        mEntryAdapter.setHasStableIds(true)
        entriesRecyclerview.layoutManager = LinearLayoutManager(this)
        entriesRecyclerview.addItemDecoration(SpacesItemDecoration(dpToPx(8)))
        entriesRecyclerview.adapter = mEntryAdapter
    }

    override fun showEntries(entries: List<Entry>) {
        mEntryAdapter.animateTo(entries)
        entriesRecyclerview.scrollToPosition(0)
    }
}
