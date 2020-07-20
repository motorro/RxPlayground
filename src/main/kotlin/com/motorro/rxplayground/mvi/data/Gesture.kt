package com.motorro.rxplayground.mvi.data

/**
 * UI gesture
 */
sealed class Gesture {
    object Add: Gesture()
    object Subtract: Gesture()
}