package com.desuzed.everyweather.domain.model.result

sealed class FetchResult<out T, out E> {
    data class Success<T>(val body: T) : FetchResult<T, Nothing>()

    data class Failure<U>(val error: U) : FetchResult<Nothing, U>()

}