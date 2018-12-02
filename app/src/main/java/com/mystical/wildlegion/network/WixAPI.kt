package com.mystical.wildlegion.network

import com.mystical.wildlegion.network.models.*
import com.mystical.wildlegion.screens.main.models.Member
import com.mystical.wildlegion.screens.main.models.News
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
    fun removeClanMember(@Body request: DeleteRequest): Completable

    @GET("news")
    fun getNews(): Observable<List<News>>

    @POST("newStory")
    fun createNewStory(@Body request: NewStoryRequest): Completable

    @POST("updateNews")
    fun editNews(@Body request: EditStoryRequest): Completable

    @POST("removeNews")
    fun deleteNews(@Body request: DeleteRequest): Completable

    @GET("recruitmentStatus")
    fun getRecruitmentStatus(): Observable<RecruitmentStatus>
}