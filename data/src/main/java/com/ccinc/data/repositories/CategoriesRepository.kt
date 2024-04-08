package com.ccinc.data.repositories

import com.ccinc.api.FoodApi
import com.ccinc.api.model.CategoriesResponse
import com.ccinc.common.Logger
import com.ccinc.data.model.Categories
import com.ccinc.data.utils.toCategoriesDBO
import com.ccinc.data.utils.toCategories
import com.ccinc.database.FoodDatabase
import com.ccinc.database.models.CategoriesDBO
import com.ccinc.data.model.RequestResult
import com.ccinc.data.utils.map
import com.ccinc.data.utils.toRequestResult
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class CategoriesRepository @Inject constructor(
    private val database: FoodDatabase,
    private val api: FoodApi,
    private val logger: Logger,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCategories(mergeStrategy: MergeStrategy<RequestResult<List<Categories>>> = RequestResponseMergeStrategy()): Flow<RequestResult<List<Categories>>> {
        return getDataFromDatabase().combine(getDataFromServer(), mergeStrategy::merge)
            .flatMapConcat { result ->
                if (result is RequestResult.Success) {
                    database.categoriesDao.observeCategories()
                        .map { list ->
                            list.map { item ->
                                item.toCategories()
                            }
                        }
                        .map {
                            RequestResult.Success(it)
                        }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getDataFromServer(): Flow<RequestResult<List<Categories>>> {

        val apiRequest = flow { emit(api.getCategories()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResultToCache(result.getOrThrow())
                } else {
                    logger.e(
                        LOG_TAG,
                        "Error getting from server. Case = ${result.exceptionOrNull()}"
                    )
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<List<CategoriesResponse>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result ->
                result.map { list ->
                    list.map { it.toCategories() }
                }
            }
    }

    private suspend fun saveNetResultToCache(data: List<CategoriesResponse>) {
        val dbo = data.map { it.toCategoriesDBO() }
        database.categoriesDao.insertCategories(dbo)
    }

    private fun getDataFromDatabase(): Flow<RequestResult<List<Categories>>> {
        val dbRequest = database.categoriesDao::getCategories.asFlow()
            .map<List<CategoriesDBO>, RequestResult<List<CategoriesDBO>>> { RequestResult.Success(it) }
            .catch {
                logger.d(LOG_TAG, "Error getting from database. Case = $it")
                emit(RequestResult.Error(error = it))
            }

        val start = flowOf<RequestResult<List<CategoriesDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
            .map { result ->
                result.map { list ->
                    list.map { it.toCategories() }
                }
            }
    }

    companion object {
        const val LOG_TAG = "CategoriesRepository"
    }

}



