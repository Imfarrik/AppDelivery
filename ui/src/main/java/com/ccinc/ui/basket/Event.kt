package com.ccinc.ui.basket

import com.ccinc.data.model.Basket

sealed class Event {
    class AddProductsInBasket(val input: Basket) : Event()
    class DecreaseOrRemoveFromBasket(val input: Basket) : Event()
}