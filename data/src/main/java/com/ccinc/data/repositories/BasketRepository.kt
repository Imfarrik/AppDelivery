package com.ccinc.data.repositories

import com.ccinc.common.Logger
import com.ccinc.data.model.Basket
import com.ccinc.data.model.RequestResult
import com.ccinc.data.utils.map
import com.ccinc.data.utils.toBasket
import com.ccinc.data.utils.toBasketDBO
import com.ccinc.database.FoodDatabase
import com.ccinc.database.models.BasketDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class BasketRepository @Inject constructor(
    private val database: FoodDatabase,
    private val logger: Logger,
) {

    suspend fun clear() {
        database.basketDao.clean()
    }

    suspend fun getBasket(): List<Basket> {
        return database.basketDao.getBasket().map { it.toBasket() }
    }

    fun observeBasket(): Flow<List<Basket>> {
//        return getDataFromDatabase()
        return database.basketDao.observeBasket()
            .map { result ->
                result.map {
                    it.toBasket()
                }
            }
    }

    suspend fun saveItemInBasket(item: Basket) {
        database.basketDao.insertProduct(item.toBasketDBO())
    }

    suspend fun removeItemInBasket(item: Basket) {
        database.basketDao.remove(item.toBasketDBO())
    }

    private fun getDataFromDatabase(): Flow<RequestResult<List<Basket>>> {
        val dbRequest = database.basketDao::getBasket.asFlow()
            .map<List<BasketDBO>, RequestResult<List<BasketDBO>>> { RequestResult.Success(it) }
            .catch {
                logger.d(LOG_TAG, "Error getting from database. Case = $it")
                emit(RequestResult.Error(error = it))
            }

        val start = flowOf<RequestResult<List<BasketDBO>>>(RequestResult.InProgress())

        return merge(start, dbRequest)
            .map { result ->
                result.map { list ->
                    list.map { it.toBasket() }
                }
            }
    }

    companion object {
        const val LOG_TAG = "BasketRepository"
    }

}



