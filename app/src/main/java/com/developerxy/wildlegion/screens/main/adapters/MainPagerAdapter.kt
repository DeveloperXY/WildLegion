package com.developerxy.wildlegion.screens.main.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.developerxy.wildlegion.screens.main.fragments.home.HomeFragment
import com.developerxy.wildlegion.screens.main.fragments.members.MembersFragment
import com.developerxy.wildlegion.screens.main.fragments.news.NewsFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    companion object {
        const val TAB_COUNT = 7
        val tabTitles = arrayOf("Home", "Members", "Academy", "About clan", "Our maps", "Recruiting", "Other clans")
    }

    override fun getItem(position: Int) = when (position) {
        0 -> NewsFragment()
        1 -> MembersFragment()
        else -> HomeFragment()
    }

    override fun getCount() = TAB_COUNT

    override fun getItemPosition(obj: Any) = POSITION_NONE

    override fun getPageTitle(position: Int) = tabTitles[position]
}