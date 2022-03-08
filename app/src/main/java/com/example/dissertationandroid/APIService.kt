package com.example.dissertationandroid

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    //API Service used to connect to my login functions etc on Google Cloud
    @POST("/api/v1/create")
    suspend fun loginUser(@Body requestBody: RequestBody): Response<ResponseBody>
}