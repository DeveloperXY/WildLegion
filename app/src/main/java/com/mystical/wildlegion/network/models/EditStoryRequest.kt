package com.mystical.wildlegion.network.models

import com.mystical.wildlegion.screens.main.models.News

data class EditStoryRequest(
        var _id: String,
        var title: String,
        var postDate: String,
        var postedBy: String,
        var newsStory: String) {
    constructor(news: News) : this(news._id, news.title, news.postDate, news.postedBy, news.newsStory)
}