package com.ccinc.ui.search

import com.ccinc.data.model.Basket
import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products

sealed class Event {
    class UpdateSearchTxt(val input: String) : Event()
    class AddProductsInBasket(val input: Products) : Event()
    class DecreaseOrRemoveFromBasket(val input: Products) : Event()
}