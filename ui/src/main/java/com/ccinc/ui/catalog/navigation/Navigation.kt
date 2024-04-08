package com.ccinc.ui.catalog.navigation

import androidx.navigation.NavGraphBuilder
import com.ccinc.data.model.Products
import com.ccinc.ui.catalog.CatalogRoute
import com.ccinc.ui.utils.animatedComposableSlideHorizontal

internal const val CATALOG_ROUTE = "CATALOG_ROUTE"

internal fun NavGraphBuilder.catalogScreen(
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
    navigateToSearch: () -> Unit
) {
    animatedComposableSlideHorizontal(route = CATALOG_ROUTE) {
        CatalogRoute(
            navigateToProductCard = navigateToProductCard,
            navigateToBasket = navigateToBasket,
            navigateToSearch = navigateToSearch
        )
    }
}