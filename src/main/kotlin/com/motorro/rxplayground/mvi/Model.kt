package com.motorro.rxplayground.mvi

import com.motorro.rxplayground.mvi.data.Action
import com.motorro.rxplayground.mvi.data.Gesture
import com.motorro.rxplayground.mvi.data.SideEffect
import com.motorro.rxplayground.mvi.data.State
import com.motorro.rxplayground.mvi.middleware.Middleware
import com.motorro.rxplayground.mvi.reducer.Reducer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Supplier
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import javafx.geometry.Side

/**
 * MVI model
 */
class Model(middleware: ObservableTransformer<SideEffect, Action>) {
    /**
     * A source of state changes
     */
    private val actions = PublishSubject.create<Action>()

    /**
     * Gestures from UI
     */
    fun dispatch(gesture: Gesture) {
        actions.onNext(Action.Gestured(gesture))
    }

    /**
     * Bootstraps state-machine
     */
    private fun bootstrap() = Reducer.Outcome(State.create(), SideEffect.Init)

    /**
     * State **updates**
     */
    val stateUpdates: Observable<Reducer.Outcome> = Observable.defer {
        // Middleware driver
        // Need a behavior subject here because the initial emission from `scan` below happens before `effects`
        // is actually subscribed
        val effects = BehaviorSubject.create<List<SideEffect>>()

        Observable.merge(actions, effects.switchMap { Observable.fromIterable(it) }.compose(middleware)).serialize()
            .doOnNext { println("Action: $it") }
            .scanWith(
                { bootstrap() },
                { soFar: Reducer.Outcome, action: Action -> Reducer.reduce(soFar.state, action) }
            )
            .doOnNext { println("Outcome: $it") }
            .doOnNext { outcome -> effects.onNext(outcome.effects) }
    }
}