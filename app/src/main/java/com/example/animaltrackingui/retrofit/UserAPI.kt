package com.example.animaltrackingui.retrofit

import com.example.animaltrackingui.db.AppUser
import com.example.animaltrackingui.db.LoginRequest
import com.example.animaltrackingui.db.RegisterRequest
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserAPI {

    @POST("user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AppUser>

    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AppUser>

    @POST("user/updateLocale")
    suspend fun updateLocale(@Body countryCode: String, @Header("Authorization") auth:String): Response<AppUser>

    @POST("user/googleLogin")
    suspend fun signInWithGoogle(@Body loginRequest: LoginRequest): Response<AppUser>

    @POST("user/completeFirstGoogleSignIn")
    suspend fun completeFirstGoogleSignIn(@Body appUser: AppUser, @Header("Authorization") auth: String): Response<AppUser>

    @PATCH("user/unsyncDevice")
    suspend fun unsyncDevice(@Body deviceID: String, @Header("Authorization") auth: String): Response<String>

    @POST("user/updateDeviceID")
    suspend fun updateDeviceID(@Body deviceIDs: JSONObject, @Header("Authorization") auth:String) : Response<String>

}