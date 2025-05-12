package com.example.animaltrackingui.retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObject {
    private const val baseUrl = "http://10.0.2.2:8080/api/"
    //private const val baseUrl = "http://192.168.0.106:8080/api/"
    //private const val baseUrl = "http://192.168.0.110:8080/api/"
    private val gson = GsonBuilder().setLenient().create()
    private lateinit var retrofit: Retrofit
    lateinit var animalRecognitionAPI: AnimalRecognitionAPI
    fun init(context: Context){
        val client = OkHttpClient.Builder().addInterceptor(RequestInterceptor(context)).connectTimeout(5, TimeUnit.SECONDS).build()
        retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
        animalRecognitionAPI = Retrofit.Builder().baseUrl("http://10.0.2.2:8000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(JacksonConverterFactory.create())
            .build().create(AnimalRecognitionAPI::class.java)
    }

    var blockRequest = MutableStateFlow(true)

    val missingPostAPI:MissingPostAPI by lazy{
        retrofit.create(MissingPostAPI::class.java)
    }

    val encounteredPostAPI:EncounteredPostAPI by lazy {
        retrofit.create(EncounteredPostAPI::class.java)
    }

    val userAPI: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }

    val reportAPI: ReportAPI by lazy {
        retrofit.create(ReportAPI::class.java)
    }

//    val animalRecognitionAPI: AnimalRecognitionAPI by lazy {
//        retrofit.create(AnimalRecognitionAPI::class.java)
//    }

    fun createService(serviceClass: Class<*>): Any{
        return retrofit.create(serviceClass)
    }
}