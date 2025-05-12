package com.example.animaltrackingui.retrofit

import com.example.animaltrackingui.db.AppUser
import com.example.animaltrackingui.db.RegisterRequest
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface TestAPI {

    @POST("user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AppUser>

    @GET("user/{email}")
    suspend fun login(@Path("email") email:String): Response<AppUser>

    @POST("user/")
    suspend fun setUser(@Body appUser: AppUser): Response<JSONObject>

    @GET("test/secure")
    suspend fun testEndpoint(@Header("Authorization") auth:String): Response<String>
}