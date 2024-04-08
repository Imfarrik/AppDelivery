package com.ccinc.data.model

sealed class RequestResult<out T : Any>(open val data: T? = null) {
    class InProgress<T : Any>(data: T? = null) : RequestResult<T>(data)
    class Success<T : Any>(override val data: T) : RequestResult<T>(data)
    class Error<T : Any>(data: T? = null, val error: Throwable? = null) : RequestResult<T>(data)
}
