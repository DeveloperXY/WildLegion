package com.developerxy.wildlegion.network

import com.developerxy.wildlegion.network.models.EditClanMemberRequest
import com.developerxy.wildlegion.network.models.NewClanMemberRequest
import com.developerxy.wildlegion.network.models.RemoveClanMemberRequest
import com.developerxy.wildlegion.screens.main.models.Member
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WixAPI {
    @GET("members")
    fun getClanMembers(): Observable<List<Member>>

    @POST("newClanMember")
    fun createNewClanMember(@Body request: NewClanMemberRequest): Completable

    @POST("updateClanMember")
    fun editClanMember(@Body request: EditClanMemberRequest): Completable

    @POST("removeClanMember")
    fun removeClanMember(@Body request: RemoveClanMemberRequest): Completable
}