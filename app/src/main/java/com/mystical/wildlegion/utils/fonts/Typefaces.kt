package com.mystical.wildlegion.utils.fonts

import android.content.res.AssetManager
import android.graphics.Typeface

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
        fun getFont(assets: AssetManager, name: String = "", style: String = "Regular"): Typeface {
            val fontName = when (name) {
                "asylum" -> asylumTemplate
                "opensans" -> String.format(openSansTemplate, style)
                "oswald" -> String.format(oswaldTemplate, style)
                "kellyslab" -> String.format(kellySlabTemplate, style)
                else -> String.format(robotoTemplate, style)
            }

            if (map.containsKey(fontName))
                return map[fontName]!!

            map[fontName] = Typeface.createFromAsset(assets, fontName)
            return map[fontName]!!
        }

        fun getSpanFont(assets: AssetManager, name: String) = CustomTypefaceSpan(getFont(assets, name))
    }
}