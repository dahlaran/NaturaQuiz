package com.dahlaran.naturaquiz.data.model

import com.google.gson.annotations.SerializedName

data class PlantsCountResponse(
    @SerializedName("plants_count") val count: Int,
    @SerializedName("detailled_plants_count") val detailedPlaint: Int
)