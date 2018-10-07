package com.developerxy.wildlegion.screens.guestbook.models

data class Entry(var date: String,
                 var name: String,
                 var number: Int,
                 var body: String) {
    constructor(): this("", "", -1, "")
}