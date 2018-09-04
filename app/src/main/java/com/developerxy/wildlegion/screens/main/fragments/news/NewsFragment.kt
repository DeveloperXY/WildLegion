package com.developerxy.wildlegion.screens.main.fragments.news


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.models.News
import com.developerxy.wildlegion.utils.SpacesItemDecoration
import com.developerxy.wildlegion.utils.dpToPx
import kotlinx.android.synthetic.main.fragment_members.*


class NewsFragment : Fragment(), NewsContract.View {

    lateinit var mPresenter: NewsPresenter
    lateinit var mMembersAdapter: NewsAdapter
    var newsFragmentDelegate: NewsFragmentDelegate? = null
    var visibility = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is NewsFragmentDelegate)
            newsFragmentDelegate = context
        mPresenter = NewsPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresenter.start()
        swipeRefreshLayout.setOnRefreshListener {
            mPresenter.loadNews()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (!isVisibleToUser && visibility)
            mPresenter.showAllNews()

        visibility = isVisibleToUser
    }

    override fun setupRecyclerView() {
        mMembersAdapter = NewsAdapter(context!!, mutableListOf())
        mMembersAdapter.onNewsSelected = { selectedNews, sharedViews ->
            newsFragmentDelegate?.onNewsSelected(selectedNews, sharedViews)
        }
        membersRecyclerview.layoutManager = LinearLayoutManager(activity)
        membersRecyclerview.addItemDecoration(SpacesItemDecoration(dpToPx(8)))
        membersRecyclerview.adapter = mMembersAdapter
    }

    override fun revertItemSwipe(position: Int) {
        val adapter = membersRecyclerview.adapter as NewsAdapter
        adapter.notifyItemChanged(position)
    }

    override fun removeMember(position: Int) {
        val adapter = membersRecyclerview.adapter as NewsAdapter
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

    override fun showNews(news: List<News>) {
        mMembersAdapter.animateTo(news)
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

    interface NewsFragmentDelegate {
        fun onNewsSelected(selectedNews: News, sharedViews: Array<View>)
    }
}
