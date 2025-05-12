package com.example.animaltrackingui.retrofit

import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.PostStatus
import retrofit2.Response
import retrofit2.http.*

interface EncounteredPostAPI {
    @GET("posts/encountered/")
    suspend fun getEncounteredPosts() : Response<List<EncounteredPost>>

    @GET("posts/encountered/")
    suspend fun getLocalEncounteredPosts(@Query("countryCode") countryCode: String, @Query("location") locations: List<String>, @Query("animal") animals: List<String>, @Header("Authorization") auth: String?) : Response<List<EncounteredPost>>

    @GET("posts/encountered/all")
    suspend fun getAllEncounteredPosts(@Query("location") locations: List<String>, @Query("animal") animals: List<String>, @Header("Authorization") auth: String?) : Response<List<EncounteredPost>>

    @GET("posts/encountered/")
    suspend fun getEncounteredPost(@Query("id") id: Long) : Response<EncounteredPost>

    @GET("posts/encountered/")
    suspend fun getUserEncounteredPosts(@Query("username") username:String, @Header("Authorization") auth:String) : Response<List<EncounteredPost>>

    @GET("posts/encountered/gov/")
    suspend fun getEncounteredPostsForGov(@Query("countryCode") countryCode: String, @Query("location") locations: List<String>, @Query("animal") animals: List<String>, @Header("Authorization") auth: String) : Response<List<EncounteredPost>>

    @GET("posts/encountered/archive/{id}")
    suspend fun archivePost(@Path("id") id: Long, @Header("Authorization") auth:String) : Response<EncounteredPost>

    @POST("posts/encountered/")
    suspend fun addEncounteredPost(@Body encounteredPost: EncounteredPost, @Header("Authorization") auth:String) : Response<EncounteredPost>

    @PATCH("posts/encountered/{id}")
    suspend fun updateEncounteredPost(@Path("id") id : Long, @Body encounteredPost: EncounteredPost, @Header("Authorization") auth:String) : Response<EncounteredPost>

    @DELETE("posts/encountered/{id}")
    suspend fun deleteEncounteredPost(@Path("id") id: Long, @Header("Authorization") auth:String) : Response<String>

    @PATCH("posts/encountered/updateStatus/{id}")
    suspend fun updateStatus(@Path("id") id: Long, @Body postStatus: PostStatus, @Header("Authorization") auth:String): Response<EncounteredPost>
}