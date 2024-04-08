package com.ccinc.data.use_cases

import com.ccinc.data.model.Basket
import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products
import com.ccinc.data.model.RequestResult
import com.ccinc.data.model.Tags
import com.ccinc.data.repositories.BasketRepository
import com.ccinc.data.repositories.CategoriesRepository
import com.ccinc.data.repositories.ProductsRepository
import com.ccinc.data.repositories.TagsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BasketUseCase @Inject constructor(
    private val basketRepository: BasketRepository,
) {

    operator fun invoke(): Flow<List<Basket>> {
        return basketRepository.observeBasket()
    }

    fun clear() {
        CoroutineScope(SupervisorJob()).launch {
            basketRepository.clear()
        }
    }

    suspend fun getBasket(): List<Basket> {
        return basketRepository.getBasket()
    }

    suspend fun updateBasket(products: Basket) {
        if (products.count < 1) {
            basketRepository.removeItemInBasket(products)
        } else {
            basketRepository.saveItemInBasket(products)
        }
    }

}
