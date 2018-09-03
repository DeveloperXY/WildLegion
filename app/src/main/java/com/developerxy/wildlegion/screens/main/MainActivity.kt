package com.developerxy.wildlegion.screens.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.BackgroundActivity
import com.developerxy.wildlegion.screens.addeditmember.AddEditClanMemberActivity
import com.developerxy.wildlegion.screens.main.adapters.MainPagerAdapter
import com.developerxy.wildlegion.screens.main.fragments.members.MembersFragment
import com.developerxy.wildlegion.screens.main.models.Member
import com.developerxy.wildlegion.utils.fonts.Typefaces
import com.developerxy.wildlegion.utils.ifSupportsLollipop
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BackgroundActivity(), MainContract.View, MembersFragment.MembersFragmentDelegate {

    private lateinit var mPagerAdapter: MainPagerAdapter
    private lateinit var mPresenter: MainPresenter
    private lateinit var mSearchView: SearchView

    companion object {
        const val REQUEST_ADD_CLAN_MEMBER = 100
        const val REQUEST_EDIT_CLAN_MEMBER = 101
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
        val searchViewItem = menu?.findItem(R.id.action_search)
        mSearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        mSearchView.setOnQueryTextListener(membersFragment)

        val searchBox = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as TextView
        searchBox.typeface = Typefaces.getFont(assets, "Light")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ADD_CLAN_MEMBER, REQUEST_EDIT_CLAN_MEMBER -> {
                if (resultCode == RESULT_OK) {
                    val membersFragment = mPagerAdapter.instantiateItem(mViewPager, 1) as MembersFragment
                    membersFragment.mPresenter.loadClanMembers()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (mSearchView.isIconified.not()) {
            mSearchView.setQuery("", false)
            mSearchView.clearFocus()
            mSearchView.isIconified = true
        } else
            super.onBackPressed()
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
                *(sharedViews.map { view ->
                    Pair.create(view, ViewCompat.getTransitionName(view))
                }.toTypedArray())
        )
        val intent = Intent(this, AddEditClanMemberActivity::class.java)
        intent.putExtra("member", selectedMember)
        intent.putExtra("isEditing", true)

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
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, -mToolbar.height as Float, 0.0f)
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
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, -mToolbar.height as Float)
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
}