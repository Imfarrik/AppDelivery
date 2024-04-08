package com.ccinc.data.repositories

import com.ccinc.data.model.RequestResult

interface MergeStrategy<E> {

    fun merge(cache: E, server: E): E

}

internal class RequestResponseMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {

    override fun merge(cache: RequestResult<T>, server: RequestResult<T>): RequestResult<T> {
        return when {
            cache is RequestResult.InProgress && server is RequestResult.InProgress ->
                merge(cache, server)

            cache is RequestResult.Success && server is RequestResult.InProgress ->
                merge(cache, server)

            cache is RequestResult.InProgress && server is RequestResult.Success ->
                merge(cache, server)

            cache is RequestResult.Success && server is RequestResult.Error ->
                merge(cache, server)

            cache is RequestResult.InProgress && server is RequestResult.Error ->
                merge(cache, server)

            cache is RequestResult.Success && server is RequestResult.Success ->
                merge(cache, server)

            cache is RequestResult.Error && server is RequestResult.InProgress ->
                merge(cache, server)

            cache is RequestResult.Error && server is RequestResult.Success ->
                merge(cache, server)

            else -> error("Unimplemented branch cache -> $cache & server -> $server")
        }
    }

    private fun merge(
        cache: RequestResult.Error<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return server
    }

    private fun merge(
        cache: RequestResult.Error<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return server
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(data = server.data)
    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return when {
            server.data != null -> RequestResult.InProgress(server.data)
            else -> RequestResult.InProgress(cache.data)
        }
    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(data = server.data ?: cache.data, error = server.error)
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(cache.data)
    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {

        return RequestResult.InProgress(server.data)
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(data = cache.data, error = server.error)
    }

}