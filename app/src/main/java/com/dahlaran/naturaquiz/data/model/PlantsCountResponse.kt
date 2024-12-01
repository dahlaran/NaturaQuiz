package com.dahlaran.naturaquiz.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model send by the API when fetching the number of plants
 */
data class PlantsCountResponse(
    @SerializedName("plants_count") val count: Int,
    @SerializedName("detailled_plants_count") val detailedPlaint: Int
)