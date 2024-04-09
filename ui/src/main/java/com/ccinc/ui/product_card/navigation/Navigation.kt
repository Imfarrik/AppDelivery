package com.ccinc.ui.product_card.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.ccinc.data.model.Products
import com.ccinc.ui.product_card.ProductCardRoute
import com.ccinc.ui.utils.animatedComposableSlideHorizontal
import com.google.gson.Gson

internal const val PRODUCT_CARD_ROUTE = "PRODUCT_CARD_ROUTE"

internal fun NavController.navigateToProductCard(
    products: Products,
    navOptions: NavOptions? = null
) {
    val productsStr = Gson().toJson(products).replace("/", "^")
    this.navigate("$PRODUCT_CARD_ROUTE/$productsStr", navOptions)
}

internal fun NavGraphBuilder.productCardScreen(onBack: () -> Unit) {
    animatedComposableSlideHorizontal(route = "$PRODUCT_CARD_ROUTE/{products}") {
        val products = Gson().fromJson(
            it.arguments?.getString("products"),
            Products::class.java
        )
        ProductCardRoute(onBack = onBack, products = products)
    }
}