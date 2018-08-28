package com.developerxy.wildlegion.screens.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.adapters.base.BaseSearchAdapter
import com.developerxy.wildlegion.screens.main.adapters.base.BinderViewHolder
import com.developerxy.wildlegion.screens.main.models.Member
import kotlinx.android.synthetic.main.member_row_layout.view.*

class MembersAdapter(context: Context, items: MutableList<Member>):
        BaseSearchAdapter<MembersAdapter.MembersViewHolder, Member>(context, items) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MembersViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.member_row_layout, parent, false))

    class MembersViewHolder(itemView: View) : BinderViewHolder<Member>(itemView) {
        override fun bind(member: Member?) {
            itemView.tvGamerangerId.text = member?.gamerangerId
            itemView.tvGamerangerNickname.text = member?.nickname
        }
    }
}