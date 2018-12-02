package com.mystical.wildlegion.customviews

import android.content.Context
import android.preference.PreferenceCategory
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.mystical.wildlegion.R
import com.mystical.wildlegion.utils.fonts.Typefaces

class MyPreferenceCategory @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : PreferenceCategory(context, attrs, defStyleAttr) {

    override fun onBindView(view: View) {
        super.onBindView(view)
        val titleView = view.findViewById<TextView>(android.R.id.title)
        titleView.typeface = Typefaces.getFont(context.assets, "Regular")

        titleView.setTextColor(context.resources.getColor(R.color.colorAccent))
    }
}