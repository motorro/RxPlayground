package com.motorro.rxplayground.mvi.middleware

import com.motorro.rxplayground.mvi.data.Action
import com.motorro.rxplayground.mvi.data.SideEffect
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer

/**
 * Middleware to run side-effects
 */
object Middleware: ObservableTransformer<SideEffect, Action> {
    override fun apply(upstream: Observable<SideEffect>): ObservableSource<Action> = upstream.publish { published ->
        Observable.merge<Action>(
            published.ofType(SideEffect.Init::class.java).flatMap { Observable.just(Action.Initialized(10)) },
            published.ofType(SideEffect.PerformAdd::class.java).flatMap { Observable.just(Action.Added(1)) },
            published.ofType(SideEffect.PerformSubtract::class.java).flatMap { Observable.just(Action.Subtracted(1)) }
        )
    }
}