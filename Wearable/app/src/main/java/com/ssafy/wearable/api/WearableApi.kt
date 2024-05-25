package com.ssafy.wearable.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WearableApi {
    @GET("api/wearable")
    fun sendLocation(
        @Query("x") x: Double,
        @Query("y") y: Double
    ): Call<ApiResponse>
}
