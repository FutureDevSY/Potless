package com.ssafy.wearable.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.potless.co.kr/")  // Ensure the base URL ends with a '/'
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WearableApi by lazy {
        retrofit.create(WearableApi::class.java)
    }
}
