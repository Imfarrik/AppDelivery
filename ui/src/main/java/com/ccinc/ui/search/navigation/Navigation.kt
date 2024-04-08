package com.ccinc.ui.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.ccinc.data.model.Products
import com.ccinc.ui.search.SearchRoute
import com.ccinc.ui.utils.animatedComposableSlideHorizontal

internal const val SEARCH_ROUTE = "SEARCH_ROUTE"

internal fun NavController.navigateToSearchScreen(navOptions: NavOptions? = null) {
    this.navigate(SEARCH_ROUTE, navOptions)
}

internal fun NavGraphBuilder.searchScreen(
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
    onBack: () -> Unit
) {
    animatedComposableSlideHorizontal(route = SEARCH_ROUTE) {
        SearchRoute(
            navigateToProductCard = navigateToProductCard,
            navigateToBasket = navigateToBasket,
            onBack = onBack
        )
    }
}