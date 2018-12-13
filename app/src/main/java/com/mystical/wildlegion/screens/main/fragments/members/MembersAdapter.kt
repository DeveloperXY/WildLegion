package com.mystical.wildlegion.screens.main.fragments.members

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mystical.wildlegion.R
import com.mystical.wildlegion.screens.main.adapters.base.BaseSearchAdapter
import com.mystical.wildlegion.screens.main.adapters.base.BinderViewHolder
import com.mystical.wildlegion.screens.main.models.Member
import com.mystical.wildlegion.screens.main.models.MemberHeader
import com.mystical.wildlegion.screens.main.models.MemberItem
import kotlinx.android.synthetic.main.member_header_row_layout.view.*
import kotlinx.android.synthetic.main.member_row_layout.view.*

class MembersAdapter(context: Context, items: MutableList<MemberItem>) :
        BaseSearchAdapter<BinderViewHolder<MemberItem>, MemberItem>(context, items) {

    companion object {
        const val HEADER_LAYOUT = 0
        const val NON_HEADER_LAYOUT = 1
    }

    var onMemberSelected: ((position: Int, selectedMember: Member, sharedViews: Array<View>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            if (viewType == NON_HEADER_LAYOUT) {
                MembersViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.member_row_layout, parent, false))
            } else {
                MembersHeaderViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.member_header_row_layout, parent, false))
            }


    override fun getItemViewType(position: Int) = when (mItems[position].isHeader()) {
        true -> HEADER_LAYOUT
        else -> NON_HEADER_LAYOUT
    }

    inner class MembersViewHolder(itemView: View) : BinderViewHolder<MemberItem>(itemView) {

        var tvGamerangerId: TextView = itemView.tvGamerangerId
        var tvGamerangerNickname: TextView = itemView.tvGamerangerNickname
        var tvGovernment: TextView = itemView.tvGovernment

        init {
            itemView.setOnClickListener {
                onMemberSelected?.invoke(adapterPosition, mItems[adapterPosition] as Member,
                        arrayOf(tvGamerangerId, tvGamerangerNickname))
            }
        }

        override fun bind(item: MemberItem?) {
            val member = item as Member
            tvGamerangerId.text = member.gamerangerId
            tvGamerangerNickname.text = member.nickname

            if (member.government != null && member.government?.isNotEmpty()!!)
                tvGovernment.visibility = VISIBLE
            else
                tvGovernment.visibility = GONE
            tvGovernment.text = when (member.government) {
                "C" -> "COUNCIL"
                "L" -> "LEADER"
                "F" -> "FOUNDER"
                else -> ""
            }
        }
    }

    inner class MembersHeaderViewHolder(itemView: View) : BinderViewHolder<MemberItem>(itemView) {

        var tvCategory: TextView = itemView.tvCategory
        var flameView1: ImageView = itemView.flameView1
        var flameView2: ImageView = itemView.flameView2

        override fun bind(item: MemberItem?) {
            Glide.with(mContext).asGif()
                    .load(R.drawable.flame)
                    .into(flameView1)
            Glide.with(mContext).asGif()
                    .load(R.drawable.flame)
                    .into(flameView2)
            var header = item as MemberHeader
            tvCategory.text = header.header
        }
    }
}