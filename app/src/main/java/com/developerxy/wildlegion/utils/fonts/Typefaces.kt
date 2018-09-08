package com.developerxy.wildlegion.utils.fonts

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
        fun getFont(assets: AssetManager, name: String = "Regular"): Typeface {
            val fontName = if (name == "asylum") asylumTemplate else
                String.format(robotoTemplate, name)
            if (map.containsKey(fontName))
                return map[fontName]!!

            map[fontName] = Typeface.createFromAsset(assets, fontName)
            return map[fontName]!!
        }

        fun getSpanFont(assets: AssetManager, name: String) = CustomTypefaceSpan(getFont(assets, name))
    }
}