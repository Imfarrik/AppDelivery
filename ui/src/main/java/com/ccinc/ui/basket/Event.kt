package com.ccinc.ui.basket

import com.ccinc.data.model.Basket
import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products

sealed class Event {
    class AddProductsInBasket(val input: Basket) : Event()
    class DecreaseOrRemoveFromBasket(val input: Basket) : Event()
}