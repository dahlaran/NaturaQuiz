package com.dahlaran.naturaquiz.data.model

import com.google.gson.annotations.SerializedName

data class Plant(
    val id: Int,
    @SerializedName("common_name") val name: String,
    @SerializedName("image_url") val imageUrl: String
)

data class PlantsResponse(
    val data: List<Plant>
)