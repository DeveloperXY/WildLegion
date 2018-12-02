package com.mystical.wildlegion.customviews

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.mystical.wildlegion.R

/**
 * A custom EditText view.
 *
 * This custom view sets a custom font dynamically based on the value of a custom attribute.
 *
 *  @author Mohammed Aouf ZOUAG
 */
class FontEnabledEditText : AppCompatEditText, FontEnabledView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyCustomFont(context, attrs)
    }

    override fun getAttributes() = R.styleable.FontEnabledButton

    override fun getCustomFont() = R.styleable.FontEnabledButton_customFont

    override fun getCustomFontStyle() = R.styleable.FontEnabledEditText_customStyle
}