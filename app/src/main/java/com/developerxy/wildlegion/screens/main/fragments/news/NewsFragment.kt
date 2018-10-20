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
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment(), NewsContract.View {
    override fun getApplication() = activity?.application!!

    lateinit var mPresenter: NewsPresenter
    lateinit var mNewsAdapter: NewsAdapter
    var newsFragmentDelegate: NewsFragmentDelegate? = null

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

    override fun setupRecyclerView() {
        mNewsAdapter = NewsAdapter(context!!, mutableListOf())
        mNewsAdapter.setHasStableIds(true)
        mNewsAdapter.onNewsSelected = { position, selectedNews, sharedViews ->
            mPresenter.doIfLoggedIn {
                newsFragmentDelegate?.onNewsSelected(position, selectedNews, sharedViews)
            }
        }
        membersRecyclerview.layoutManager = NpaLinearLayoutManager(context!!)
        membersRecyclerview.addItemDecoration(SpacesItemDecoration(dpToPx(8)))
        membersRecyclerview.adapter = mNewsAdapter
    }

    override fun onResume() {
        super.onResume()
        mShimmerLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        mShimmerLayout.stopShimmer()
    }

    override fun removeNews(position: Int) {
        val adapter = membersRecyclerview.adapter as NewsAdapter
        adapter.removeItem(position)
    }

    override fun showMemberRemovalFailedError() {
        Toast.makeText(activity, "There was an error while trying to remove this clan member.",
                Toast.LENGTH_LONG).show()
    }

    override fun showNews(news: List<News>) {
        mNewsAdapter.animateTo(news)
        membersRecyclerview.scrollToPosition(0)
    }

    override fun showShimmer() {
        mShimmerLayout.visibility = VISIBLE
        mShimmerLayout.startShimmer()
    }

    override fun stopShimmer() {
        mShimmerLayout.visibility = GONE
        mShimmerLayout.stopShimmer()
    }

    override fun showLoadingError(error: Throwable) {
        backgroundText.visibility = VISIBLE
    }

    override fun hideLoadingError() {
        if (backgroundText.visibility == VISIBLE)
            backgroundText.visibility = GONE
    }

    override fun stopRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }

    interface NewsFragmentDelegate {
        fun onNewsSelected(position: Int, selectedNews: News, sharedViews: Array<View>)
    }

    /**
     * Fixes a RecyclerView consistency error.
     * No idea how it works though lol
     */
    private class NpaLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        /**
         * Disable predictive animations. There is a bug in RecyclerView which causes views that
         * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
         * adapter size has decreased since the ViewHolder was recycled.
         */
        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }
}
