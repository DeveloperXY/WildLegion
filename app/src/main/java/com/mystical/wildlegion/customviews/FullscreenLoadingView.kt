package com.mystical.wildlegion.customviews

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.mystical.wildlegion.R
import kotlinx.android.synthetic.main.fullscreen_loading_view_layout.view.*

/**
 *  @author Mohammed Aouf ZOUAG
 */
class FullscreenLoadingView : ConstraintLayout {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.fullscreen_loading_view_layout, this, true)
    }

    fun setStatement(text: String) {
        tvStatus.text = text
    }

    fun stopLoading() {
        successImage.visibility = VISIBLE
        progressBar.visibility = GONE
    }
}