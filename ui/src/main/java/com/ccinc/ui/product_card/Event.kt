package com.ccinc.ui.product_card

import com.ccinc.data.model.Basket
import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products

sealed class Event {
    class AddProductsInBasket(val input: Products) : Event()
    class DecreaseOrRemoveFromBasket(val input: Products) : Event()
}