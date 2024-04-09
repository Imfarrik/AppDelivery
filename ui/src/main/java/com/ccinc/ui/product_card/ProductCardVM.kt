package com.ccinc.ui.product_card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccinc.data.model.Basket
import com.ccinc.data.model.Products
import com.ccinc.data.model.Tags
import com.ccinc.data.repositories.TagsRepository
import com.ccinc.data.use_cases.BasketUseCase
import com.ccinc.data.utils.toBasket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class ProductCardVM @Inject constructor(
    private val basketUseCase: Provider<BasketUseCase>,
    private val tagsRepository: Provider<TagsRepository>,
) : ViewModel() {

    val productsInBasket: StateFlow<List<Basket>> =
        basketUseCase.get().invoke().stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    var tags: List<Tags> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            tags = tagsRepository.get().getTags()
        }
    }


    private fun addProductsInBasket(input: Products) = with(viewModelScope) {
        launch {

            val find = basketUseCase.get().getBasket().find {
                it.id == input.id
            }

            if (find == null) {
                basketUseCase.get()
                    .updateBasket(input.toBasket())
            } else {
                basketUseCase.get()
                    .updateBasket(find.copy(count = find.count + 1))
            }
        }
    }

    private fun decreaseOrRemoveFromBasket(input: Products) = with(viewModelScope) {
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