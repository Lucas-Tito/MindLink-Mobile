package com.titolucas.mindlink.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    //NÃO MUDA ESSE URL A NÃO SER QUE DÊ ERRO (10.0.2.2 no android studio corresponde ao ipv4 do host)
    private const val BASE_URL = "http://10.0.2.2:3000/mindlink/"
    //private const val BASE_URL = "http://172.21.0.1:3000/mindlink/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
