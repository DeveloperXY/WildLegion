package com.mystical.wildlegion.network.models

data class NewStoryRequest(
        var title: String,
        var postDate: String,
        var newsStory: String,
        var postedBy: String
)