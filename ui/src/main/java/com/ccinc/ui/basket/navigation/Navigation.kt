package com.ccinc.ui.basket.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.ccinc.ui.basket.BasketRoute
import com.ccinc.ui.utils.animatedComposableSlideHorizontal

internal const val BASKET_ROUTE = "BASKET_ROUTE"

internal fun NavController.navigateToBasket(navOptions: NavOptions? = null) {
    this.navigate(BASKET_ROUTE, navOptions)
}

internal fun NavGraphBuilder.basketScreen(onBack: () -> Unit) {
    animatedComposableSlideHorizontal(route = BASKET_ROUTE) {
        BasketRoute(onBack = onBack)
    }
}