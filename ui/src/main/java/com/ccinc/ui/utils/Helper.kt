package com.ccinc.ui.utils

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

internal fun String?.checkForNull(): String {
    return this ?: ""
}

internal fun Long?.checkForNull(): String {
    return this?.toString() ?: ""
}

fun Long?.toReadableFormat(isDecimal: Boolean = true): String {
    if (this == null) return ""
    val mSymbols = DecimalFormatSymbols()
    mSymbols.groupingSeparator = ' '
    mSymbols.decimalSeparator = '.'
    val mBalanceFormat = if (isDecimal) {
        DecimalFormat("###,##0.##", mSymbols)
    } else {
        val f = DecimalFormat("###,##0", mSymbols)
        f.roundingMode = RoundingMode.DOWN
        f
    }

    return mBalanceFormat.format((this.toDouble().div(100)))
}

fun NavGraphBuilder.animatedComposableSlideHorizontal(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 500 })
        },
        exitTransition = {
            fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { - 500 })
        },
        popExitTransition = {
            fadeOut()
        })
    {
        content(it)
    }
}