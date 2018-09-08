package com.developerxy.wildlegion.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.preference.SwitchPreference
import android.util.AttributeSet
import android.view.View


class MySwitchPreference : SwitchPreference, CustomFontPreference {

    @SuppressLint("NewApi")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onBindView(view: View) {
        super.onBindView(view)
        applyFont(context, view)
    }
}