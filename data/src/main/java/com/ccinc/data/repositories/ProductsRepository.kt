package com.ccinc.data.repositories

import com.ccinc.api.FoodApi
import com.ccinc.api.model.ProductsResponse
import com.ccinc.common.Logger
import com.ccinc.database.FoodDatabase
import com.ccinc.data.model.Products
import com.ccinc.data.model.RequestResult
import com.ccinc.data.utils.toProductsDBO
import com.ccinc.data.utils.toProduct
import com.ccinc.data.utils.map
import com.ccinc.data.utils.toRequestResult
import com.ccinc.database.models.ProductsDBO
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

class ProductsRepository @Inject constructor(
    private val database: FoodDatabase,
    private val api: FoodApi,
    private val logger: Logger,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getProducts(mergeStrategy: MergeStrategy<RequestResult<List<Products>>> = RequestResponseMergeStrategy()): Flow<RequestResult<List<Products>>> {
        return getDataFromDatabase().combine(getDataFromServer(), mergeStrategy::merge)
            .flatMapConcat { result ->
                if (result is RequestResult.Success) {
                    database.productsDao.observeProducts()
                        .map { list ->
                            list.map { item ->
                                item.toProduct()
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

    private fun getDataFromServer(): Flow<RequestResult<List<Products>>> {

        val apiRequest = flow { emit(api.getProducts()) }
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

        val start = flowOf<RequestResult<List<ProductsResponse>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result ->
                result.map { list ->
                    list.map { it.toProduct() }
                }
            }
    }

    private fun getDataFromDatabase(): Flow<RequestResult<List<Products>>> {
        val dbRequest = database.productsDao::getProducts.asFlow()
            .map<List<ProductsDBO>, RequestResult<List<ProductsDBO>>> { RequestResult.Success(it) }
            .catch {
                logger.d(LOG_TAG, "Error getting from database. Case = $it")
                emit(RequestResult.Error(error = it))
            }

        val start = flowOf<RequestResult<List<ProductsDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
            .map { result ->
                result.map { list ->
                    list.map { it.toProduct() }
                }
            }
    }

    private suspend fun saveNetResultToCache(data: List<ProductsResponse>) {
        val dbo = data.map { it.toProductsDBO() }
        database.productsDao.insertProducts(dbo)
    }

    companion object {
        const val LOG_TAG = "ProductsRepository"
    }

}
