package com.example.animaltrackingui.retrofit

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AnimalRecognitionAPI {

    @GET("animalTypes")
    suspend fun getAnimalTypes(@Header("Authorization") auth:String,@Query("pet") pet: Boolean) : Response<List<String>>

    @Multipart
    @POST("identifyAnimal")
    suspend fun identifyAnimal(@Part image: MultipartBody.Part,@Header("Authorization") auth:String) : Response<String>
}