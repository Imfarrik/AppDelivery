package com.ccinc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ccinc.ui.basket.navigation.basketScreen
import com.ccinc.ui.basket.navigation.navigateToBasket
import com.ccinc.ui.catalog.navigation.CATALOG_ROUTE
import com.ccinc.ui.catalog.navigation.catalogScreen
import com.ccinc.ui.product_card.navigation.navigateToProductCard
import com.ccinc.ui.product_card.navigation.productCardScreen
import com.ccinc.ui.search.navigation.navigateToSearchScreen
import com.ccinc.ui.search.navigation.searchScreen

@Composable
fun RootNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = CATALOG_ROUTE
    ) {

        catalogScreen(
            navigateToProductCard = navController::navigateToProductCard,
            navigateToBasket = navController::navigateToBasket,
            navigateToSearch = navController::navigateToSearchScreen
        )

        productCardScreen(onBack = navController::popBackStack)

        basketScreen(onBack = navController::popBackStack)

        searchScreen(
            navigateToBasket = navController::navigateToBasket,
            navigateToProductCard = navController::navigateToProductCard,
            onBack = navController::popBackStack
        )

    }
}


private object Graph {
    const val ROOT = "root_graph"
}