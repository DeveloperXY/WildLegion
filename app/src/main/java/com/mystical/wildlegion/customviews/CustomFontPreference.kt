package com.mystical.wildlegion.customviews

import android.content.Context
import android.view.View
import android.widget.TextView
import com.mystical.wildlegion.R
import com.mystical.wildlegion.utils.fonts.Typefaces

interface CustomFontPreference {
    fun applyFont(context: Context, rootView: View) {
        val title = rootView.findViewById<TextView>(android.R.id.title)
        val summary = rootView.findViewById<TextView>(android.R.id.summary)
        sequenceOf(title, summary).forEach {
            it.typeface = Typefaces.getFont(context.assets)
        }

        title.setTextColor(context.resources.getColor(R.color.field_text_color))
        summary.setTextColor(context.resources.getColor(R.color.quantum_grey_600))
    }
}