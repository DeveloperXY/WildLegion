package com.developerxy.wildlegion.customviews

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.StyleableRes
import android.util.AttributeSet
import com.developerxy.wildlegion.utils.fonts.Typefaces

/**
 * An interface indicating that the implementing class (view) can apply custom fonts.
 *
 * The interface is applicable to any view with the [setTypeface] method,
 *  and hence it requires the [getCustomFont] method to be overridden
 *  to be able to determine from where to load the custom attributes.
 *
 *  @author Mohammed Aouf ZOUAG
 */
interface FontEnabledView {
    fun applyCustomFont(context: Context?, attrs: AttributeSet?) {
        val typedArray = context!!.theme.obtainStyledAttributes(
                attrs, getAttributes(), 0, 0)

        try {
            val font = typedArray.getInt(getCustomFont(), -1)
            if (font == 0)
                setTypeface(Typefaces.getFont(context.assets, "asylum"))
            else if (font == 1) {
                val style = when (typedArray.getInt(getCustomFont(), -1)) {
                    0 -> "Regular"
                    1 -> "Light"
                    2 -> "Bold"
                    else -> "Regular"
                }
                setTypeface(Typefaces.getFont(context.assets, style))
            }

        } finally {
            typedArray.recycle()
        }
    }

    /**
     * Returns an [IntArray] of the attributes of the View implementing this interface.
     *
     * @return an array of attributes, identified by name as in the declaration of
     * [&lt;declare-styleable&gt;] element in the **attrs.xml** file.
     */
    @StyleableRes
    fun getAttributes(): IntArray

    /**
     * Returns a styleable resource indicating the element from which
     * to load custom font attribute value.
     *
     * @return the styleable resource to use
     */
    @StyleableRes
    fun getCustomFont(): Int

    /**
     * Returns a styleable resource indicating the element from which
     * to load the custom font's style.
     *
     * @return the styleable resource to use
     */
    @StyleableRes
    fun getCustomFontStyle(): Int

    /**
     * Sets a typeface on a target view.
     *
     * @param typeface the typeface to set
     */
    fun setTypeface(typeface: Typeface)
}