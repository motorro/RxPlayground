package com.motorro.rxplayground.mvi.reducer

import com.motorro.rxplayground.mvi.data.Action
import com.motorro.rxplayground.mvi.data.Gesture
import com.motorro.rxplayground.mvi.data.SideEffect
import com.motorro.rxplayground.mvi.data.State
import io.reactivex.rxjava3.functions.BiFunction

/**
 * State reducer
 */
object Reducer {
    /**
     * State-reduction outcome
     * Contains:
     * @property state New state
     * @property effects Side-effects to drive async operations through middleware
     */
    data class Outcome(val state: State, val effects: List<SideEffect> = emptyList()) {
        /**
         * Shortcut for single side-effect
         */
        constructor(state: State, effect: SideEffect): this(state, listOf(effect))
    }

    /**
     * Reduces state
     */
    fun reduce(soFar: State, action: Action): Outcome = when(action) {
        is Action.Gestured -> when(action.gesture) {
            Gesture.Add -> Outcome(soFar, SideEffect.PerformAdd)
            Gesture.Subtract -> Outcome(soFar, SideEffect.PerformSubtract)
        }
        is Action.Initialized -> Outcome(soFar.copy(value = action.amount))
        is Action.Added -> Outcome(soFar.copy(value = soFar.value + action.amount))
        is Action.Subtracted -> Outcome(soFar.copy(value = soFar.value - action.amount))
    }
}