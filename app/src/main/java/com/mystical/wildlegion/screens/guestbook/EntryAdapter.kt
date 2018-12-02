package com.mystical.wildlegion.screens.guestbook

import android.content.Context
import android.os.Build
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mystical.wildlegion.R
import com.mystical.wildlegion.screens.guestbook.models.Entry
import com.mystical.wildlegion.screens.main.adapters.base.BaseSearchAdapter
import com.mystical.wildlegion.screens.main.adapters.base.BinderViewHolder
import com.mystical.wildlegion.utils.dpToPx
import kotlinx.android.synthetic.main.entry_row_layout.view.*

class EntryAdapter(context: Context, items: MutableList<Entry>) :
        BaseSearchAdapter<EntryAdapter.EntryViewHolder, Entry>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            EntryViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.entry_row_layout, parent, false))

    override fun getItemId(position: Int): Long = position.toLong()

    inner class EntryViewHolder(itemView: View) : BinderViewHolder<Entry>(itemView) {

        var tvDate: TextView = itemView.tvDate
        var tvName: TextView = itemView.tvName
        var tvNumber: TextView = itemView.tvNumber
        var tvBody: TextView = itemView.tvBody

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                (itemView as CardView).apply {
                    cardElevation = dpToPx(8).toFloat()
                }
            }
        }

        override fun bind(entry: Entry) {
            tvDate.text = entry.date
            tvName.text = entry.name
            tvNumber.text = entry.number.toString()
            tvBody.text = entry.body
        }
    }
}