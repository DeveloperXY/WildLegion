package com.developerxy.wildlegion.screens.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.adapters.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Glide.with(this)
                .load(R.drawable.wild_legion_full)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(wildLegionLogo)

        mPagerAdapter = MainPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter)

        for (index in 1..mTabLayout.tabCount) {
            val textView = LayoutInflater.from(this).inflate(R.layout.single_tab_layout, null) as TextView
            mTabLayout.getTabAt(index - 1)?.customView = textView
        }
    }
}