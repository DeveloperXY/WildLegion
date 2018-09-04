package com.developerxy.wildlegion.screens.main.fragments.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.adapters.base.BaseSearchAdapter
import com.developerxy.wildlegion.screens.main.adapters.base.BinderViewHolder
import com.developerxy.wildlegion.screens.main.models.News
import kotlinx.android.synthetic.main.news_row_layout.view.*

class NewsAdapter(context: Context, items: MutableList<News>) :
        BaseSearchAdapter<NewsAdapter.NewsViewHolder, News>(context, items) {

    var onNewsSelected: ((selectedNews: News, sharedViews: Array<View>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NewsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_row_layout, parent, false))

    override fun getItemId(position: Int): Long = position.toLong()

    inner class NewsViewHolder(itemView: View) : BinderViewHolder<News>(itemView) {

        var tvTitle: TextView = itemView.tvTitle
        var tvDate: TextView = itemView.tvDate
        var tvStoryNews: TextView = itemView.tvStoryNews
        var tvPostedBy: TextView = itemView.tvPostedBy

        init {
            itemView.setOnClickListener {

            }
        }

        override fun bind(news: News) {
            tvTitle.text = news.title
            tvDate.text = news.postDate
            tvStoryNews.text = news.newsStory
            tvPostedBy.text = news.postedBy
        }
    }
}