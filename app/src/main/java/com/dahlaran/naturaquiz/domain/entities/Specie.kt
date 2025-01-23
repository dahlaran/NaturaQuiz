package com.dahlaran.naturaquiz.domain.entities

import com.google.gson.annotations.SerializedName

class Specie(
    val id: Int,
    @SerializedName("common_name") val name: String?,
    @SerializedName("scientific_name") val scientificName: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("year") val discoveredYear: Int?,
    @SerializedName("family") val family: String?,
    @SerializedName("genus") val genus: String?,
) {
    fun getNotNullName(): String {
        return if (name?.isNotEmpty() == true) name else scientificName ?: ""
    }
}