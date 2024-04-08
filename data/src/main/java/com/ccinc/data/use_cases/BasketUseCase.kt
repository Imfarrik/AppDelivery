package com.ccinc.data.use_cases

import com.ccinc.data.model.Basket
import com.ccinc.data.repositories.BasketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
