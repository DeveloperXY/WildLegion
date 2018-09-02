package com.developerxy.wildlegion.network.models

import com.developerxy.wildlegion.screens.main.models.Member

data class EditClanMemberRequest(
        var _id: String,
        var nickname: String,
        var gamerangerId: String,
        var rank: String) {
    constructor(member: Member) : this(member._id, member.nickname, member.gamerangerId, member.rank + "")
}