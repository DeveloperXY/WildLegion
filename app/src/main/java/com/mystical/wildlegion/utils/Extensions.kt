package com.mystical.wildlegion.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Build

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

inline fun Activity.ifSupportsLollipop(action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        action()
}