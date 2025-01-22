package com.dahlaran.naturaquiz.domain.entities

import com.google.gson.annotations.SerializedName

data class Plant(
    val id: Int,
    @SerializedName("common_name") val name: String?,
    @SerializedName("scientific_name") val scientificName: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("year") val discoveredYear: Int?,
    @SerializedName("family") val family: String?,
    @SerializedName("genus") val genus: String?,
) {
    fun isEligibleForQuiz(): Boolean {
        return imageUrl?.isNotEmpty() == true && (name?.isNotEmpty() == true || scientificName?.isNotEmpty() == true)
    }

    fun isEligibleForWrongAnswer(): Boolean {
        return name?.isNotEmpty() == true || scientificName?.isNotEmpty() == true
    }

    fun getNotNullName(): String {
        return if (name?.isNotEmpty() == true) {
            name
        } else {
            scientificName ?: ""
        }
    }
}
