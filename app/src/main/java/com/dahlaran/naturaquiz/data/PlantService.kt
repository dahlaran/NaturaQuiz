package com.dahlaran.naturaquiz.data

import com.dahlaran.naturaquiz.data.model.PlantsCountResponse
import com.dahlaran.naturaquiz.data.model.PlantsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface PlantService {
    @GET("")
    suspend fun getPlantsCount(): Response<PlantsCountResponse>


    @GET("plants")
    suspend fun getPlants(@Query("page") page: Int, @Query("limit") limit: Int = 10): Response<PlantsResponse>
}