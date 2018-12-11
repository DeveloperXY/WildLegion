package com.mystical.wildlegion.network.models

import com.mystical.wildlegion.screens.main.models.Member

data class EditClanMemberRequest(
        var _id: String,
        var nickname: String,
        var gamerangerId: String,
        var rank: String,
        var isActive: Boolean) {
    constructor(member: Member) : this(member._id, member.nickname, member.gamerangerId,
            member.rank + "", member.isActive)
}