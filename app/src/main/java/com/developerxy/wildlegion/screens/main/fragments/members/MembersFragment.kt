package com.developerxy.wildlegion.screens.main.fragments.members


import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.models.Member
import com.developerxy.wildlegion.utils.SpacesItemDecoration
import com.developerxy.wildlegion.utils.SwipeToDeleteCallback
import com.developerxy.wildlegion.utils.dpToPx
import kotlinx.android.synthetic.main.fragment_members.*


class MembersFragment : Fragment(), MembersContract.View {

    lateinit var mPresenter: MembersPresenter
    lateinit var mMembersAdapter: MembersAdapter
    var membersFragmentDelegate: MembersFragmentDelegate? = null
    var visibility = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MembersFragmentDelegate)
            membersFragmentDelegate = context
        mPresenter = MembersPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_members, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresenter.start()
        swipeRefreshLayout.setOnRefreshListener {
            mPresenter.loadClanMembers()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (!isVisibleToUser && visibility)
            mPresenter.showAllMembers()

        visibility = isVisibleToUser
    }

    override fun setupRecyclerView() {
        mMembersAdapter = MembersAdapter(context!!, mutableListOf())
        mMembersAdapter.onMemberSelected = { selectedMember, sharedViews ->
            membersFragmentDelegate?.onMemberSelected(selectedMember, sharedViews)
        }
        membersRecyclerview.layoutManager = LinearLayoutManager(activity)
        membersRecyclerview.addItemDecoration(SpacesItemDecoration(dpToPx(8)))
        membersRecyclerview.adapter = mMembersAdapter

        val swipeHandler = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = membersRecyclerview.adapter as MembersAdapter
                val position = viewHolder.adapterPosition
                val member = adapter.items[position]
                mPresenter.removeClanMember(member, position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(membersRecyclerview)
    }

    override fun revertItemSwipe(position: Int) {
        val adapter = membersRecyclerview.adapter as MembersAdapter
        adapter.notifyItemChanged(position)
    }

    override fun removeMember(position: Int) {
        val adapter = membersRecyclerview.adapter as MembersAdapter
        adapter.removeItem(position)
    }

    override fun showMemberRemovedMessage(memberName: String) {
        Toast.makeText(activity, "$memberName was removed from the clan.",
                Toast.LENGTH_LONG).show()
    }

    override fun showMemberRemovalFailedError() {
        Toast.makeText(activity, "There was an error while trying to remove this clan member.",
                Toast.LENGTH_LONG).show()
    }

    override fun showMembers(members: List<Member>) {
        mMembersAdapter.animateTo(members)
        membersRecyclerview.scrollToPosition(0)
    }

    override fun showLoadingError(error: Throwable) {
        backgroundText.visibility = VISIBLE
    }

    override fun hideLoadingError() {
        if (backgroundText.visibility == VISIBLE)
            backgroundText.visibility = GONE
    }

    override fun showProgressbar() {
        progressBar.visibility = VISIBLE
    }

    override fun hideProgressbar() {
        if (progressBar.visibility == VISIBLE)
            progressBar.visibility = GONE
    }

    override fun stopRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onQueryTextSubmit(query: String?) = false

    override fun onQueryTextChange(newText: String?): Boolean {
        mPresenter.onSearchQueryTextChange(newText)
        return true
    }

    interface MembersFragmentDelegate {
        fun onMemberSelected(selectedMember: Member, sharedViews: Array<View>)
    }
}
