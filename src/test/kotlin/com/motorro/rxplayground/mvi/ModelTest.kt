package com.motorro.rxplayground.mvi

import com.motorro.rxplayground.mvi.data.Action
import com.motorro.rxplayground.mvi.data.Gesture
import com.motorro.rxplayground.mvi.data.SideEffect
import com.motorro.rxplayground.mvi.data.State
import com.motorro.rxplayground.mvi.middleware.Middleware
import com.motorro.rxplayground.mvi.reducer.Reducer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class ModelTest {
    @Test
    fun startsWithBootstrap() {
        val model = Model(ObservableTransformer { it.switchMap { Observable.empty<Action>() } })
        model.stateUpdates.test().assertValue(Reducer.Outcome(State.create(), SideEffect.Init))
    }

    @Test
    fun initializesMiddlewareWithBootstrap() {
        val sideEffects: MutableList<SideEffect> = LinkedList()
        val middleware: ObservableTransformer<SideEffect, Action> = ObservableTransformer { upstream ->
            upstream.switchMap {
                Completable.fromAction { sideEffects.add(it) }.andThen<Action>(Observable.empty())
            }
        }
        val model = Model(middleware)
        model.stateUpdates.test().assertValue(Reducer.Outcome(State.create(), SideEffect.Init))
        assertEquals(
                listOf(SideEffect.Init),
                sideEffects
        )
    }

    @Test
    fun bootstrapsMvi() {
        val model = Model(Middleware)
        model.stateUpdates.test().assertValues(
            Reducer.Outcome(State.create(), SideEffect.Init), // Bootstrap
            Reducer.Outcome(State(value = 10)) // State reduced with Action.Initialized
        )
    }

    @Test
    fun processesUi() {
        val model = Model(Middleware)
        val o = model.stateUpdates.test()
        model.dispatch(Gesture.Add)
        model.dispatch(Gesture.Subtract)
        o.assertValues(
            Reducer.Outcome(State.create(), SideEffect.Init), // Bootstrap
            Reducer.Outcome(State(value = 10)), // State reduced with Action.Initialized
            Reducer.Outcome(State(value = 10), SideEffect.PerformAdd), // Action.Gestured is to be processed by SideEffect.PerformAdd
            Reducer.Outcome(State(value = 11)), // State reduced with Action.Added
            Reducer.Outcome(State(value = 11), SideEffect.PerformSubtract), // Action.Gestured is to be processed by SideEffect.PerformSubtract
            Reducer.Outcome(State(value = 10)) // State reduced with Action.Subtracted
        )
    }
}