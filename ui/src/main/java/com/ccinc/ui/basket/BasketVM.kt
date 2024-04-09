package com.ccinc.ui.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccinc.data.model.Basket
import com.ccinc.data.use_cases.BasketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class BasketVM @Inject constructor(
    private val basketUseCase: Provider<BasketUseCase>,
) : ViewModel() {

    val productsInBasket: StateFlow<List<Basket>> =
        basketUseCase.get().invoke().stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    private fun addProductsInBasket(input: Basket) = with(viewModelScope) {
        launch {

            val find = basketUseCase.get().getBasket().find {
                it.id == input.id
            }

            if (find == null) {
                basketUseCase.get()
                    .updateBasket(input)
            } else {
                basketUseCase.get()
                    .updateBasket(find.copy(count = find.count + 1))
            }
        }
    }

    private fun decreaseOrRemoveFromBasket(input: Basket) = with(viewModelScope) {
        launch {
            val find = basketUseCase.get().getBasket().find {
                it.id == input.id
            }

            find?.let { it1 ->
                basketUseCase.get()
                    .updateBasket(
                        it1.copy(count = it1.count - 1)
                    )
            }
        }
    }

    fun sentEvent(event: Event) {
        when (event) {

            is Event.AddProductsInBasket -> addProductsInBasket(event.input)

            is Event.DecreaseOrRemoveFromBasket -> decreaseOrRemoveFromBasket(event.input)
        }
    }

}