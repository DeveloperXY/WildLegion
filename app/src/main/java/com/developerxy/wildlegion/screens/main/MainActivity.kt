package com.developerxy.wildlegion.screens.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.addmember.AddClanMemberActivity
import com.developerxy.wildlegion.screens.main.adapters.MainPagerAdapter
import com.developerxy.wildlegion.screens.main.fragments.members.MembersFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var mPagerAdapter: MainPagerAdapter
    private lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ifSupportsLollipop {
            window.enterTransition = null
        }
        setContentView(R.layout.activity_main)

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

    override fun displayBackgroundImages() {
        Glide.with(this)
                .load(R.drawable.wild_legion_full)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(wildLegionLogo)

        Glide.with(this)
                .load(R.drawable.members_load_failed)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(backgroundImage)
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

            private fun handleFabVisibility(position: Int) = when(position) {
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
            startActivity(Intent(this, AddClanMemberActivity::class.java))
        }
    }

    private fun ifSupportsLollipop(action: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            action()
    }
}