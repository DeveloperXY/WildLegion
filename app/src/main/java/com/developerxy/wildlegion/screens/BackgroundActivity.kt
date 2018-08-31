package com.developerxy.wildlegion.screens

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.developerxy.wildlegion.R
import kotlinx.android.synthetic.main.activity_main.*

abstract class BackgroundActivity: AppCompatActivity() {
    fun onCreate(savedInstanceState: Bundle?, @LayoutRes layout: Int) {
        super.onCreate(savedInstanceState)
        setContentView(layout)

        Glide.with(this)
                .load(R.drawable.activity_background)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(backgroundImage)
    }
}