package com.motorro.rxplayground.mvi.data

/**
 * Side-effect to drive middleware
 */
sealed class SideEffect {
    object Init: SideEffect()
    object PerformAdd: SideEffect()
    object PerformSubtract: SideEffect()
}