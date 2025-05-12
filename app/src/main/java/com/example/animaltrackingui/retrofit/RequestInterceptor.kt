package com.example.animaltrackingui.retrofit

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import com.example.animaltrackingui.AnimalTrackingApplication
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

class RequestInterceptor(private val applicationContext: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response?
        val request = chain.request()
        try {
            response = if (!RetrofitObject.blockRequest.value) {
                chain.proceed(request)
            } else {
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(418)
                    .message("No Network")
                    .body(ResponseBody.create(null, "Error")).build()
            }
        }
        catch (exception: Exception){
            exception.localizedMessage?.let { Log.e("Retrofit Exception", it) }
            response = Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(418)
                .message("No Server Response")
                .body(ResponseBody.create(null, "Error")).build()
        }
        if(request.header("Authorization") != null && response?.code() == 401){
            val scope = CoroutineScope(Dispatchers.IO)
            val application = applicationContext as AnimalTrackingApplication
            scope.launch {
                application.container.appRepository.removeUser()
                application.container.mGoogleSignInClient.signOut()
                Firebase.auth.signOut()
                val intent = Intent(Intent.ACTION_VIEW,"animal_tracking_app://main/".toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)

            }
        }
        Log.i("Url",request.url().toString())
        Log.i("Response Code", response?.code().toString())
        if(response?.header("Custom-Message") != null){
            Log.i("Message", response.header("Custom-Message")!!)
        }
        else{
            response?.message()?.let { Log.i("Message", it) }
        }
        return response!!
    }
}