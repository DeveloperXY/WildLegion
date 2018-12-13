package com.mystical.wildlegion.screens.main.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mystical.wildlegion.screens.main.fragments.aboutclan.AboutClanFragment
import com.mystical.wildlegion.screens.main.fragments.members.MembersFragment
import com.mystical.wildlegion.screens.main.fragments.news.NewsFragment
import com.mystical.wildlegion.screens.main.fragments.recruiting.RecruitingFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    companion object {
        val tabTitles = arrayOf("News", "Members", "About clan", "Recruiting")
        val TAB_COUNT = tabTitles.size
    }

    override fun getItem(position: Int) = when (position) {
        0 -> NewsFragment()
        1 -> MembersFragment()
        2 -> AboutClanFragment()
        else -> RecruitingFragment()
    }

    override fun getCount() = TAB_COUNT

    override fun getItemPosition(obj: Any) = POSITION_NONE

    override fun getPageTitle(position: Int) = tabTitles[position]
}