package com.ccinc.ui.catalog

import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products
import com.ccinc.data.model.Tags

sealed class Event {
    class UpdateSelectedCategories(val input: Categories) : Event()
    class AddProductsInBasket(val input: Products) : Event()
    class DecreaseOrRemoveFromBasket(val input: Products) : Event()
    class UpdateFilterList(val input: Tags) : Event()
    data object IsSheetVisible : Event()
}