package com.developerxy.wildlegion.screens.main.fragments.news

import android.content.Context
import android.os.Build
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.adapters.base.BaseSearchAdapter
import com.developerxy.wildlegion.screens.main.adapters.base.BinderViewHolder
import com.developerxy.wildlegion.screens.main.models.News
import com.developerxy.wildlegion.utils.dpToPx
import kotlinx.android.synthetic.main.news_row_layout.view.*

class NewsAdapter(context: Context, items: MutableList<News>) :
        BaseSearchAdapter<NewsAdapter.NewsViewHolder, News>(context, items) {

    var onNewsSelected: ((position: Int, selectedNews: News, sharedViews: Array<View>) -> Unit)? = null

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                (itemView as CardView).apply {
                    cardElevation = dpToPx(8).toFloat()
                }
            }

            itemView.setOnClickListener {
                onNewsSelected?.invoke(adapterPosition, mItems[adapterPosition],
                        arrayOf(tvTitle, tvDate, tvStoryNews))
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