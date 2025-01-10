package com.titolucas.mindlink.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://192.168.1.2:3000/mindlink/"
//    private const val BASE_URL = "http://172.25.215.8:3000/mindlink/"
    private const val BASE_URL = "http://192.168.0.4:3000/mindlink/"


    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
