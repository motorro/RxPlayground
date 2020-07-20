package com.motorro.rxplayground.mvi.data

/**
 * Application state
 */
data class State(val value: Int) {
    companion object {
        fun create() = State(0)
    }
}