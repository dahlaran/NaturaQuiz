package com.dahlaran.naturaquiz.domain.entities

data class Quiz(
    val goodAnswer: Plant,
    val wrongAnswer: Plant,
    val leftIsGoodAnswer: Boolean,
) {


    fun getLeftAnswerText(): String {
        return getLeftAnswer().getNotNullName()
    }

    fun getRightAnswerText(): String {
        return getRightAnswer().getNotNullName()
    }

    private fun getLeftAnswer(): Plant {
        return if (leftIsGoodAnswer) goodAnswer else wrongAnswer
    }

    private fun getRightAnswer(): Plant {
        return if (leftIsGoodAnswer) wrongAnswer else goodAnswer
    }
}