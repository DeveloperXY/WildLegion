package com.mystical.wildlegion.screens.main.models

interface MemberItem {
    fun isHeader(): Boolean
    fun getHeaderContent(): String?
}