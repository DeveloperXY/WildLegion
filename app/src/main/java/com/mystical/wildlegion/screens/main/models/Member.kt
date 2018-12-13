package com.mystical.wildlegion.screens.main.models

import java.io.Serializable

data class Member(var _id: String,
                  var gamerangerId: String,
                  var nickname: String,
                  var rank: Char,
                  var isActive: Boolean,
                  var government: String? = "") : MemberItem, Serializable {
    override fun isHeader() = false

    override fun getHeaderContent(): Nothing? = null
}