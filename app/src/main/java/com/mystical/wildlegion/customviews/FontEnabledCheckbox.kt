package com.mystical.wildlegion.customviews

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.util.AttributeSet
import com.mystical.wildlegion.R

/**
 * A custom Checkbox view.
 *
 * This custom view sets a custom font dynamically based on the value of a custom attribute.
 *
 *  @author Mohammed Aouf ZOUAG
 */
class FontEnabledCheckbox : AppCompatCheckBox, FontEnabledView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyCustomFont(context, attrs)
    }

    override fun getAttributes() = R.styleable.FontEnabledCheckbox

    override fun getCustomFont() = R.styleable.FontEnabledCheckbox_customFont

    override fun getCustomFontStyle() = R.styleable.FontEnabledCheckbox_customStyle
}