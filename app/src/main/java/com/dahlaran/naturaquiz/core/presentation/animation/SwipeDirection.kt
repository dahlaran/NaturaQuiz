package com.dahlaran.naturaquiz.core.presentation.animation

/**
 * Enum class to represent the swipe direction
 *
 * @param multiplier the value to multiply the swipe distance
 */
enum class SwipeDirection(val multiplier: Float) {
    LEFT(-1f),
    RIGHT(1f)
}