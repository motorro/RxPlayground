package com.motorro.rxplayground.mvi.data

/**
 * Reducer action
 */
sealed class Action {
    data class Gestured(val gesture: Gesture): Action()
    data class Initialized(val amount: Int): Action()
    data class Added(val amount: Int): Action()
    data class Subtracted(val amount: Int): Action()
}