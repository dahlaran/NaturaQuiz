package com.dahlaran.naturaquiz.data

import com.dahlaran.naturaquiz.data.model.PlantsCountResponse
import com.dahlaran.naturaquiz.data.model.PlantsResponse
import com.dahlaran.naturaquiz.data.model.SpeciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface PlantService {
    @GET("v1/")
    suspend fun getPlantsCount(): Response<PlantsCountResponse>


    @GET("v1/plants")
    suspend fun getPlants(@Query("page") page: Int): Response<PlantsResponse>

    @GET("v1/species")
    suspend fun getSpecies(): Response<SpeciesResponse>
}