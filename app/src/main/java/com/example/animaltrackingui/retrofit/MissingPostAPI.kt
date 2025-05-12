package com.example.animaltrackingui.retrofit

import com.example.animaltrackingui.db.MissingPost
import com.example.animaltrackingui.db.PostStatus
import retrofit2.Response
import retrofit2.http.*

interface MissingPostAPI {

    @GET("posts/missing/")
    suspend fun getMissingPosts() : Response<List<MissingPost>>

    @GET("posts/missing/all")
    suspend fun getAllMissingPosts(@Query("location") locations: List<String>, @Query("animal") animals: List<String>, @Header("Authorization") auth: String?) : Response<List<MissingPost>>

    @GET("posts/missing/")
    suspend fun getLocalMissingPosts(@Query("countryCode") countryCode: String, @Query("location") locations: List<String>, @Query("animal") animals: List<String>, @Header("Authorization") auth: String?) : Response<List<MissingPost>>

    @GET("posts/missing/")
    suspend fun getMissingPost(@Query("id") id: Long) : Response<MissingPost>

    @GET("posts/missing/")
    suspend fun getUserMissingPosts(@Query("username") username:String,@Header("Authorization") auth:String) : Response<List<MissingPost>>

    @GET("posts/missing/gov/")
    suspend fun getMissingPostsForGov(@Query("countryCode") countryCode: String, @Query("location") locations: List<String>, @Query("animal") animals: List<String>, @Header("Authorization") auth: String) : Response<List<MissingPost>>

    @GET("posts/missing/archive/{id}")
    suspend fun archivePost(@Path("id") id: Long, @Header("Authorization") auth:String) : Response<MissingPost>

    @POST("posts/missing/")
    suspend fun addMissingPost(@Body missingPost: MissingPost, @Header("Authorization") auth:String) : Response<MissingPost>

    @PATCH("posts/missing/{id}")
    suspend fun updateMissingPost(@Path("id") id : Long, @Body missingPost: MissingPost, @Header("Authorization") auth:String) : Response<MissingPost>

    @DELETE("posts/missing/{id}")
    suspend fun deleteMissingPost(@Path("id") id: Long, @Header("Authorization") auth:String) : Response<String>

    @PATCH("posts/missing/updateStatus/{id}")
    suspend fun updateStatus(@Path("id") id: Long, @Body postStatus: PostStatus, @Header("Authorization") auth:String): Response<MissingPost>
}