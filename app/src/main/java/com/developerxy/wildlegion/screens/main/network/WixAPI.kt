package com.developerxy.wildlegion.screens.main.network

import com.developerxy.wildlegion.screens.main.models.Member
import io.reactivex.Observable
import retrofit2.http.GET

interface WixAPI {
    @GET("members")
    fun getClanMembers(): Observable<List<Member>>
}