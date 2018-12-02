package com.mystical.wildlegion.network.models

data class NewClanMemberRequest(
        var nickname: String,
        var gamerangerId: String,
        var rank: String
)