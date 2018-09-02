package com.developerxy.wildlegion.screens.main.models

import java.io.Serializable

data class Member(var _id: String,
                  var gamerangerId: String,
                  var nickname: String,
                  var rank: Char) : Serializable