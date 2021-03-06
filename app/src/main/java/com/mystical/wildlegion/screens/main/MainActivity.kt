package com.mystical.wildlegion.screens.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.*
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mystical.wildlegion.R
import com.mystical.wildlegion.data.UserRepository
import com.mystical.wildlegion.screens.BackgroundActivity
import com.mystical.wildlegion.screens.addeditmember.AddEditClanMemberActivity
import com.mystical.wildlegion.screens.addeditstory.AddEditNewsStoryActivity
import com.mystical.wildlegion.screens.login.LoginActivity
import com.mystical.wildlegion.screens.main.adapters.MainPagerAdapter
import com.mystical.wildlegion.screens.main.fragments.aboutclan.AboutClanFragment
import com.mystical.wildlegion.screens.main.fragments.members.MembersFragment
import com.mystical.wildlegion.screens.main.fragments.news.NewsFragment
import com.mystical.wildlegion.screens.main.models.Member
import com.mystical.wildlegion.screens.main.models.News
import com.mystical.wildlegion.screens.settings.SettingsActivity
import com.mystical.wildlegion.utils.ResultCodes.Companion.MEMBER_ADDED
import com.mystical.wildlegion.utils.ResultCodes.Companion.MEMBER_DELETED
import com.mystical.wildlegion.utils.ResultCodes.Companion.MEMBER_UPDATED
import com.mystical.wildlegion.utils.ResultCodes.Companion.NEWS_ADDED
import com.mystical.wildlegion.utils.ResultCodes.Companion.NEWS_DELETED
import com.mystical.wildlegion.utils.ResultCodes.Companion.NEWS_UPDATED
import com.mystical.wildlegion.utils.fonts.Typefaces
import com.mystical.wildlegion.utils.ifSupportsLollipop
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_items.view.*


class MainActivity : BackgroundActivity(), MainContract.View,
        NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mPagerAdapter: MainPagerAdapter
    private lateinit var mPresenter: MainPresenter
    private lateinit var mSearchView: SearchView
    private lateinit var mUserRepository: UserRepository
    private lateinit var mNewsItem: MenuItem
    private lateinit var mMembersItem: MenuItem
    private lateinit var mAboutClanItem: MenuItem
    private lateinit var mRecruitingItem: MenuItem
    private lateinit var mAuthItem: MenuItem

    companion object {
        const val REQUEST_ADD_CLAN_MEMBER = 100
        const val REQUEST_EDIT_CLAN_MEMBER = 101
        const val REQUEST_ADD_NEWS_STORY = 102
        const val REQUEST_EDIT_NEWS_STORY = 103
        const val REQUEST_LOGIN_ONGOING = 104
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState, R.layout.activity_main)
        ifSupportsLollipop {
            window.enterTransition = null
        }

        mUserRepository = UserRepository.getInstance(this)

        mPresenter = MainPresenter(this)
        mPresenter.start()
    }

    override fun displayCurrentUsername(username: String) {
        val header = mNavigationView.getHeaderView(0)
        header.tvNickname.text = username
    }

    override fun displayCurrentUserEmail(email: String) {
        val header = mNavigationView.getHeaderView(0)
        header.tvEmail.text = email
    }

    override fun setupNavigationView() {
        val toggle = ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        mNavigationView.setNavigationItemSelectedListener(this)
        mNewsItem = mNavigationView.menu.findItem(R.id.nav_news)
        mMembersItem = mNavigationView.menu.findItem(R.id.nav_members)
        mAboutClanItem = mNavigationView.menu.findItem(R.id.nav_about_clan)
        mRecruitingItem = mNavigationView.menu.findItem(R.id.nav_recruiting)
        mAuthItem = mNavigationView.menu.findItem(R.id.nav_sign_out)

        val states = Array(5) { IntArray(1) }
        states[0][0] = android.R.attr.state_checked
        states[1][0] = android.R.attr.state_enabled
        states[2][0] = android.R.attr.state_pressed
        states[3][0] = android.R.attr.state_focused
        states[4][0] = android.R.attr.state_pressed
        val colors = IntArray(5)
        colors[0] = Color.parseColor("#ffffff")
        colors[1] = Color.parseColor(getString(R.color.primaryDarkColor))
        colors[2] = Color.parseColor("#ffffff")
        colors[3] = Color.parseColor("#ffffff")
        colors[4] = Color.parseColor("#ffffff")
        val navMenuTextList = ColorStateList(states, colors)
        mNavigationView.itemTextColor = navMenuTextList
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val membersFragment = mPagerAdapter.instantiateItem(mViewPager, 1) as MembersFragment
        val searchViewItem = menu?.findItem(R.id.action_search)
        mSearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        mSearchView.setOnQueryTextListener(membersFragment)

        val searchBox = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as TextView
        searchBox.typeface = Typefaces.getFont(this, "Light")
        searchBox.setTextColor(resources.getColor(R.color.field_text_color))

        MenuItemCompat.setOnActionExpandListener(searchViewItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Called when SearchView is collapsing
                if (searchViewItem?.isActionViewExpanded!!) {
                    animateSearchToolbar(1, true, false)
                }
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                appbar.setExpanded(false)
                // Called when SearchView is expanding
                animateSearchToolbar(1, true, true)
                return true
            }
        })

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

    override fun onResume() {
        super.onResume()

        mPresenter.doIf(
                ifLoggedIn = {
                    if (mViewPager.currentItem == 0 || mViewPager.currentItem == 1)
                        showFab()
                    mAuthItem?.title = "Sign out"
                },
                ifLoggedOut = {
                    hideFab()
                    mAuthItem?.title = "Sign in"
                }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ADD_CLAN_MEMBER, REQUEST_EDIT_CLAN_MEMBER -> {
                val membersFragment = mPagerAdapter.instantiateItem(mViewPager, 1) as MembersFragment
                when (resultCode) {
                    MEMBER_ADDED -> {
                        val snackbar = Snackbar.make(mFab,
                                "Would you like to publish a news story about this recruitment ?",
                                Snackbar.LENGTH_LONG)
                        snackbar.setAction("YES") {
                            val i = Intent(this, AddEditNewsStoryActivity::class.java)
                            i.putExtra("nickname", data?.getStringExtra("nickname"))
                            i.putExtra("gamerangerId", data?.getStringExtra("gamerangerId"))
                            i.putExtra("rank", data?.getStringExtra("rank"))
                            // this process is chained to a member addition operation
                            i.putExtra("chained", true)
                            startActivityForResult(i, REQUEST_ADD_NEWS_STORY)
                        }.show()
                        membersFragment.mPresenter.loadClanMembers()
                    }
                    MEMBER_UPDATED -> {
                        membersFragment.mPresenter.loadClanMembers()
                    }
                    MEMBER_DELETED -> {
                        val deletedPosition = data?.getIntExtra("position", -1)!!
                        if (deletedPosition != -1)
                            membersFragment.removeMember(deletedPosition)
                    }
                }
            }
            REQUEST_ADD_NEWS_STORY, REQUEST_EDIT_NEWS_STORY -> {
                val newsFragment = mPagerAdapter.instantiateItem(mViewPager, 0) as NewsFragment
                when (resultCode) {
                    NEWS_ADDED, NEWS_UPDATED -> newsFragment.mPresenter.loadNews()
                    NEWS_DELETED -> {
                        val deletedPosition = data?.getIntExtra("position", -1)!!
                        if (deletedPosition != -1) {
                            Log.i("IMPORTANT", "#$deletedPosition")
                            newsFragment.removeNews(deletedPosition)
                        }
                    }
                }
            }
            REQUEST_LOGIN_ONGOING -> {
                when (resultCode) {
                    RESULT_OK -> {
                        mPresenter.updateNavigationHeaderInfo()
                        val snackbar = Snackbar.make(mFab,
                                "Logged in as ${data?.getStringExtra("nickname")}.",
                                Snackbar.LENGTH_LONG)
                        snackbar.setAction("Hide") {
                            snackbar.dismiss()
                        }.show()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        when {
            mSearchView.isIconified.not() -> {
                mSearchView.setQuery("", false)
                mSearchView.clearFocus()
                mSearchView.isIconified = true
            }
            mDrawerLayout.isDrawerOpen(GravityCompat.START) -> mDrawerLayout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
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

        for (index in 1..mTabLayout.tabCount) {
            val textView = LayoutInflater.from(this).inflate(R.layout.single_tab_layout, null) as TextView
            mTabLayout.getTabAt(index - 1)?.customView = textView
        }
    }

    override fun setFabClickListener() {
        mFab.setOnClickListener {
            val currentPage = mViewPager.currentItem
            when (currentPage) {
                0 -> {
                    startActivityForResult(Intent(this, AddEditNewsStoryActivity::class.java),
                            REQUEST_ADD_NEWS_STORY)
                }
                1 -> {
                    startActivityForResult(Intent(this, AddEditClanMemberActivity::class.java),
                            REQUEST_ADD_CLAN_MEMBER)
                }
            }
        }
    }

    override fun showFab() {
        mFab.show()
    }

    override fun hideFab() {
        mFab.hide()
    }

    override fun onNewsSelected(position: Int, selectedNews: News, sharedViews: Array<View>) {
        val intent = Intent(this, AddEditNewsStoryActivity::class.java)
        intent.putExtra("news", selectedNews)
        intent.putExtra("isEditing", true)
        intent.putExtra("position", position)

        startActivityForResult(intent, REQUEST_EDIT_NEWS_STORY)
    }

    override fun onMemberSelected(position: Int, selectedMember: Member, sharedViews: Array<View>) {
        val intent = Intent(this, AddEditClanMemberActivity::class.java)
        intent.putExtra("member", selectedMember)
        intent.putExtra("isEditing", true)
        intent.putExtra("position", position)

        startActivityForResult(intent, REQUEST_EDIT_CLAN_MEMBER)
    }

    fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.search_toolbar_bg))

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val width = mToolbar.width -
                        (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0) -
                        resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2
                val createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        if (false) mToolbar.width - width else width, mToolbar.height / 2, 0.0f, width.toFloat())
                createCircularReveal.duration = 250
                createCircularReveal.start()
            } else {
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, (-mToolbar.height).toFloat(), 0.0f)
                translateAnimation.duration = 220
                mToolbar.clearAnimation()
                mToolbar.startAnimation(translateAnimation)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val width = mToolbar.width -
                        (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0) -
                        resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2
                val createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        if (false) mToolbar.width - width else width, mToolbar.height / 2, width.toFloat(), 0.0f)
                createCircularReveal.duration = 250
                createCircularReveal.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        mToolbar.setBackgroundResource(R.drawable.toolbar_bg)
                    }
                })
                createCircularReveal.start()
            } else {
                val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, (-mToolbar.height).toFloat())
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(alphaAnimation)
                animationSet.addAnimation(translateAnimation)
                animationSet.duration = 220
                animationSet.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        mToolbar.setBackgroundResource(R.drawable.toolbar_bg)
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                mToolbar.startAnimation(animationSet)
            }
        }
    }

    override fun getContext() = this

    override fun clearViewPagerChangeListeners() {
        mViewPager.clearOnPageChangeListeners()
    }

    override fun addViewPagerChangeListener() {
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            var lastPosition = 0

            override fun onPageScrollStateChanged(state: Int) {
                mPresenter.doIfLoggedIn {
                    when (state) {
                        SCROLL_STATE_DRAGGING -> mFab.hide()
                        SCROLL_STATE_IDLE -> {
                            when (lastPosition) {
                                0 -> showFabWithIcon(R.drawable.ic_create_post)
                                1 -> showFabWithIcon(R.drawable.baseline_add_24)
                                else -> hideFab()
                            }
                        }
                    }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mPresenter.doIfLoggedIn {
                    if (positionOffset != 0f && positionOffset != 1f) {
                        if (mFab.visibility == VISIBLE)
                            hideFab()
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> mNewsItem.isChecked = true
                    1 -> mMembersItem.isChecked = true
                    2 -> {
                        mAboutClanItem.isChecked = true
                        val aboutClanFragment = mPagerAdapter.instantiateItem(mViewPager, 2) as AboutClanFragment
                        aboutClanFragment.initializeYoutubePlayer()
                    }
                    3 -> {
                        mRecruitingItem.isChecked = true
                        appbar.setExpanded(false, true)
                    }
                }
                lastPosition = position
                invalidateOptionsMenu()
            }

            private fun showFabWithIcon(@DrawableRes fabIcon: Int) {
                mFab.setImageResource(fabIcon)
                mFab.show()
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val currentItem = when (item.itemId) {
            R.id.nav_news -> 0
            R.id.nav_members -> 1
            R.id.nav_about_clan -> 2
            R.id.nav_recruiting -> 3
            else -> -1
        }
        if (currentItem != -1)
            mViewPager.setCurrentItem(currentItem, true)

        when (item.itemId) {
            R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.nav_sign_out -> {
                mPresenter.doIf(
                        ifLoggedIn = {
                            mUserRepository.removeAll()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeBy(
                                            onError = {
                                                Toast.makeText(this, "Unexpected error.", Toast.LENGTH_LONG).show()
                                            },
                                            onComplete = {
                                                finish()
                                            }
                                    )
                        },
                        ifLoggedOut = {
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("ongoing", true)
                            startActivityForResult(intent, REQUEST_LOGIN_ONGOING)
                        }
                )
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}