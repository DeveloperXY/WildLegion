package com.mystical.wildlegion.customviews

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.mystical.wildlegion.R

/**
 * A custom TextView class.
 *
 * This custom view sets a custom font dynamically based on the value of a custom attribute.
 *
 *  @author Mohammed Aouf ZOUAG
 */
class FontEnabledTextView : AppCompatTextView, FontEnabledView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyCustomFont(context, attrs)
    }

    override fun getAttributes() = R.styleable.FontEnabledTextView

    override fun getCustomFont() = R.styleable.FontEnabledTextView_customFont

    override fun getCustomFontStyle() = R.styleable.FontEnabledTextView_customStyle
}