package com.mystical.wildlegion.screens.main.models

class MemberHeader(var header: String) : MemberItem {
    override fun isHeader() = true

    override fun getHeaderContent() = header
}