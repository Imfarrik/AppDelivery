package com.ccinc.ui.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccinc.data.model.Basket
import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products
import com.ccinc.data.model.RequestResult
import com.ccinc.data.model.Tags
import com.ccinc.data.use_cases.BasketUseCase
import com.ccinc.data.use_cases.CatalogUIModel
import com.ccinc.data.use_cases.CatalogUseCase
import com.ccinc.data.utils.toBasket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class CatalogVM @Inject constructor(
    catalogUseCase: Provider<CatalogUseCase>,
    private val basketUseCase: Provider<BasketUseCase>,
) : ViewModel() {

    val state: StateFlow<State> =
        catalogUseCase.get().invoke().map { requestResult -> requestResult.toState() }
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Undefined)

    val productsInBasket: StateFlow<List<Basket>> =
        basketUseCase.get().invoke().stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    var selectedCategories: Categories? by mutableStateOf(null)
        private set

    var isFilterVisible: Boolean by mutableStateOf(false)
        private set

    var filterList: List<Tags> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            state.collect {
                if (it is State.Success) {
                    it.data?.categories?.first()?.let { it1 -> updateSelectedCategories(it1) }
                }
            }
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

    private fun updateSelectedCategories(input: Categories) {
        selectedCategories = input
    }

    private fun updateIsFilterVisible() {
        isFilterVisible = !isFilterVisible
    }

    private fun updateFilterList(input: Tags) {
        val list = filterList.toMutableList()
        if (list.contains(input)) {
            list.remove(input)
        } else {
            list.add(input)
        }
        filterList = list
    }

    fun sentEvent(event: Event) {
        when (event) {
            is Event.UpdateSelectedCategories -> updateSelectedCategories(event.input)

            is Event.AddProductsInBasket -> addProductsInBasket(event.input)

            is Event.DecreaseOrRemoveFromBasket -> decreaseOrRemoveFromBasket(event.input)

            is Event.IsSheetVisible -> updateIsFilterVisible()

            is Event.UpdateFilterList -> updateFilterList(event.input)
        }
    }

}

private fun RequestResult<CatalogUIModel>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(data)
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
}

internal sealed class State(val data: CatalogUIModel?) {

    data object Undefined : State(data = null)

    class Loading(data: CatalogUIModel? = null) : State(data)

    class Error(data: CatalogUIModel? = null) : State(data)

    class Success(data: CatalogUIModel) : State(data)

}