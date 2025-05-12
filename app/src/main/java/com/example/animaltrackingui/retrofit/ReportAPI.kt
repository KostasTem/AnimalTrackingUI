package com.example.animaltrackingui.retrofit

import com.example.animaltrackingui.db.Report
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportAPI {

    @POST("report/")
    suspend fun sendReport(@Body report: Report): Response<Report>


}