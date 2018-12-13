package com.mystical.wildlegion.utils.fonts

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.mystical.wildlegion.R

/**
 * A utility class to load and cache fonts.
 *
 * This class loads & caches fonts for later reuse.
 */
class Typefaces {
    companion object {
        val asylumTemplate = "black_asylum.ttf"
        val robotoTemplate = "Roboto-%s.ttf"
        val openSansTemplate = "OpenSans-%s.ttf"
        val oswaldTemplate = "Oswald-%s.ttf"
        val kellySlabTemplate = "KellySlab-%s.otf"
        /**
         * A map of typefaces that were loaded, indexed by their names.
         */
        val map: MutableMap<String, Typeface> = mutableMapOf()

        /**
         * This method loads a font from assets.
         *
         * If the requested font was already requested (is already in the [map of loaded fonts][Typefaces.map]),
         * it returns that existing typeface; If not, it loads the font, saves it before returning it.
         */
        fun getFont(context: Context, name: String = "", style: String = "Regular"): Typeface {
            val fontName = when (name) {
                "asylum" -> asylumTemplate
                "opensans" -> String.format(openSansTemplate, style)
                "oswald" -> String.format(oswaldTemplate, style)
                "kellyslab" -> String.format(kellySlabTemplate, style)
                else -> String.format(robotoTemplate, style)
            }

            if (map.containsKey(fontName))
                return map[fontName]!!

            map[fontName] = ResourcesCompat.getFont(context, when (fontName) {
                "black_asylum.ttf" -> R.font.black_asylum
                "OpenSans-Bold.ttf" -> R.font.opensans_bold
                "Oswald-Regular.ttf" -> R.font.oswald_regular
                "Roboto-Bold.ttf" -> R.font.roboto_bold
                "Roboto-Light.ttf" -> R.font.roboto_light
                "KellySlab-Regular.otf" -> R.font.kellyslab_regular
                else -> R.font.roboto_regular
            })!!
            return map[fontName]!!
        }

        fun getSpanFont(context: Context, name: String) = CustomTypefaceSpan(getFont(context, name))
    }
}