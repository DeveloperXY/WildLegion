package com.developerxy.wildlegion.screens.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.BackgroundActivity
import com.developerxy.wildlegion.screens.addeditmember.AddEditClanMemberActivity
import com.developerxy.wildlegion.screens.main.adapters.MainPagerAdapter
import com.developerxy.wildlegion.screens.main.fragments.members.MembersFragment
import com.developerxy.wildlegion.screens.main.models.Member
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BackgroundActivity(), MainContract.View, MembersFragment.MembersFragmentDelegate {

    private lateinit var mPagerAdapter: MainPagerAdapter
    private lateinit var mPresenter: MainPresenter

    companion object {
        const val REQUEST_ADD_CLAN_MEMBER = 100
        const val REQUEST_EDIT_CLAN_MEMBER = 101
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState, R.layout.activity_main)
        ifSupportsLollipop {
            window.enterTransition = null
        }

        mPresenter = MainPresenter(this)
        mPresenter.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val membersFragment = mPagerAdapter.instantiateItem(mViewPager, 1) as MembersFragment
        val item = menu?.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(membersFragment)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val searchItem = menu?.findItem(R.id.action_search)
        if (mViewPager.currentItem == 1)
            searchItem?.isVisible = true
        else {
            if (searchItem?.isVisible!!)
                searchItem.isVisible = false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_ADD_CLAN_MEMBER, REQUEST_EDIT_CLAN_MEMBER -> {
                if (resultCode == RESULT_OK) {
                    val membersFragment = mPagerAdapter.instantiateItem(mViewPager, 1) as MembersFragment
                    membersFragment.mPresenter.loadClanMembers()
                }
            }
        }
    }

    override fun displayWlLogo() {
        Glide.with(this)
                .load(R.drawable.wild_legion_full)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(wildLegionLogo)
    }

    override fun initializeActionBar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.title = ""
    }

    override fun setupTabLayout() {
        mPagerAdapter = MainPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.offscreenPageLimit = 6
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                invalidateOptionsMenu()
                handleFabVisibility(position)
            }

            private fun handleFabVisibility(position: Int) = when (position) {
                1 -> mFab.visibility = VISIBLE
                else -> mFab.visibility = INVISIBLE
            }

        })

        for (index in 1..mTabLayout.tabCount) {
            val textView = LayoutInflater.from(this).inflate(R.layout.single_tab_layout, null) as TextView
            mTabLayout.getTabAt(index - 1)?.customView = textView
        }
    }

    override fun setFabClickListener() {
        mFab.setOnClickListener {
            startActivityForResult(Intent(this, AddEditClanMemberActivity::class.java),
                    REQUEST_ADD_CLAN_MEMBER)
        }
    }

    override fun onMemberSelected(selectedMember: Member, sharedViews: Array<View>) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                *(sharedViews.map {
                    view -> Pair.create(view, ViewCompat.getTransitionName(view))
                }.toTypedArray())
        )
        val intent = Intent(this, AddEditClanMemberActivity::class.java)
        intent.putExtra("member", selectedMember)
        intent.putExtra("isEditing", true)

        startActivityForResult(intent, REQUEST_EDIT_CLAN_MEMBER)
    }

    private inline fun ifSupportsLollipop(action: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            action()
    }
}