package com.developerxy.wildlegion.screens.main.fragments.members

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.adapters.base.BaseSearchAdapter
import com.developerxy.wildlegion.screens.main.adapters.base.BinderViewHolder
import com.developerxy.wildlegion.screens.main.models.Member
import kotlinx.android.synthetic.main.member_row_layout.view.*

class MembersAdapter(context: Context, items: MutableList<Member>) :
        BaseSearchAdapter<MembersAdapter.MembersViewHolder, Member>(context, items) {

    var onMemberSelected: ((position: Int, selectedMember: Member, sharedViews: Array<View>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MembersViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.member_row_layout, parent, false))

    inner class MembersViewHolder(itemView: View) : BinderViewHolder<Member>(itemView) {

        var tvGamerangerId: TextView = itemView.tvGamerangerId
        var tvGamerangerNickname: TextView = itemView.tvGamerangerNickname

        init {
            itemView.setOnClickListener {
                onMemberSelected?.invoke(adapterPosition, mItems[adapterPosition],
                        arrayOf(tvGamerangerId, tvGamerangerNickname))
            }
        }

        override fun bind(member: Member?) {
            tvGamerangerId.text = member?.gamerangerId
            tvGamerangerNickname.text = member?.nickname
        }
    }
}