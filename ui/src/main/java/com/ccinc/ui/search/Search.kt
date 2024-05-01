package com.ccinc.ui.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFilter
import androidx.hilt.navigation.compose.hiltViewModel
import com.ccinc.data.model.Basket
import com.ccinc.data.model.Products
import com.ccinc.data.use_cases.CatalogUIModel
import com.ccinc.design_system.GrayBg
import com.ccinc.design_system.Primary
import com.ccinc.ui.R
import com.ccinc.ui.utils.checkForNull
import com.ccinc.ui.utils.toReadableFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun SearchRoute(
    onBack: () -> Unit,
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
) {
    Search(
        vm = hiltViewModel(),
        onBack = onBack,
        navigateToProductCard = navigateToProductCard,
        navigateToBasket = navigateToBasket
    )
}

@Composable
private fun Search(
    vm: SearchVM,
    onBack: () -> Unit,
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
) {
    val productsInBasket by vm.productsInBasket.collectAsState()
    val state by vm.state.collectAsState()

    val currentState = state

    SearchScreen(
        onBack = onBack,
        currentState = currentState,
        productsInBasket = productsInBasket,
        sentEvent = vm::sendEvent,
        navigateToProductCard = navigateToProductCard,
        navigateToBasket = navigateToBasket,
        txtToFilter = vm.searchTxt
    )
}

@Composable
private fun SearchScreen(
    onBack: () -> Unit,
    currentState: State,
    productsInBasket: List<Basket>,
    sentEvent: (Event) -> Unit,
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
    txtToFilter: String,
) {
    currentState.data?.let { data ->
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopBar(
                    onBack = onBack,
                    searchTxt = txtToFilter,
                    sendEvent = sentEvent,
                )
            },
            bottomBar = {
                if (productsInBasket.isNotEmpty()) BottomBar(
                    productsInBasket = productsInBasket,
                    navigateToBasket = navigateToBasket
                )
            }
        ) {
            ProductsRow(
                paddingValues = it,
                data = data,
                productsInBasket = productsInBasket,
                sentEvent = sentEvent,
                txtToFilter = txtToFilter,
                navigateToProductCard = navigateToProductCard
            )
        }
    }
}

@Composable
@Preview
private fun BottomBar(
    productsInBasket: List<Basket> = listOf(),
    navigateToBasket: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            onClick = navigateToBasket,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {

            // Todo: has to be in vm

            var sum = 0L
            productsInBasket.forEach {
                sum += (it.priceCurrent ?: 0) * it.count
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.cart), contentDescription = "cart")
                Text(text = "${sum.toReadableFormat()} ₽")
            }

        }
    }
}

@Composable
private fun ProductsRow(
    paddingValues: PaddingValues,
    data: CatalogUIModel,
    productsInBasket: List<Basket>,
    sentEvent: (Event) -> Unit,
    txtToFilter: String,
    navigateToProductCard: (Products) -> Unit
) {

    // todo: has to be in vm
    data.products?.let { products ->

        val filteredProducts = if (txtToFilter == "") {
            products
        } else {
            products.fastFilter { it.name?.uppercase()?.contains(txtToFilter.uppercase()) == true }
        }

        if (filteredProducts.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(items = filteredProducts) {
                    ProductsItem(
                        item = it,
                        productsInBasket = productsInBasket,
                        sentEvent = sentEvent,
                        navigateToProductCard = navigateToProductCard
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_such_foods),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
private fun ProductsItem(
    item: Products = Products.fake(),
    productsInBasket: List<Basket> = listOf(),
    sentEvent: (Event) -> Unit = {},
    navigateToProductCard: (Products) -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .width(170.dp),
        shape = RoundedCornerShape(8.dp),
        color = GrayBg,
        onClick = { navigateToProductCard(item) }
    ) {
        Column {
            Box {
                Image(
                    modifier = Modifier.size(170.dp),
                    painter = painterResource(id = R.drawable.food),
                    contentDescription = "food"
                )
                item.priceOld?.let {
                    Image(
                        modifier = Modifier.padding(8.dp),
                        painter = painterResource(id = R.drawable.tag),
                        contentDescription = "tag"
                    )
                }
            }
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = item.name.checkForNull(), maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.basicMarquee(spacing = MarqueeSpacing(20.dp)),
                    text = "${item.measure} ${item.measureUnit}",
                    maxLines = 1,
                    color = SpanStyle().color.copy(.6f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (productsInBasket.find { it.id == item.id } == null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    onClick = { sentEvent(Event.AddProductsInBasket(item)) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 12.dp),
                            text = "${item.priceCurrent.toReadableFormat()} ₽",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        )
                        item.priceOld?.let {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.padding(vertical = 12.dp),
                                text = "${it.toReadableFormat()} ₽",
                                textDecoration = TextDecoration.LineThrough,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W400,
                                    color = SpanStyle().color.copy(.6f)
                                )
                            )
                        }

                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        onClick = {
                            sentEvent(
                                Event.DecreaseOrRemoveFromBasket(
                                    item
                                )
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp
                    ) {
                        Image(
                            modifier = Modifier.padding(8.dp),
                            painter = painterResource(id = R.drawable.minus),
                            contentDescription = "minus"
                        )
                    }
                    Text(
                        text = productsInBasket.find { it.id == item.id } !!.count.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        )
                    )
                    Surface(
                        onClick = { sentEvent(Event.AddProductsInBasket(item)) },
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp
                    ) {
                        Image(
                            modifier = Modifier.padding(8.dp),
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "plus"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

    }
}

@Composable
@Preview(showBackground = true)
private fun TopBar(
    onBack: () -> Unit = {},
    searchTxt: String = "",
    sendEvent: (Event) -> Unit = {}
) {
    Surface(color = Color.White, shadowElevation = 12.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val scope = rememberCoroutineScope()

            IconButton(modifier = Modifier.padding(vertical = 5.dp), onClick = onBack) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_left_orange),
                    contentDescription = "arrow_left_orange"
                )
            }
            OutlinedTextField(
                value = searchTxt,
                onValueChange = {
                    sendEvent(Event.UpdateSearchTxt(it))
                },
                placeholder = {
                    Text(text = stringResource(R.string.look_for_food))
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color.Black.copy(alpha = .6f),
                    focusedPlaceholderColor = Color.Black.copy(alpha = .6f),
                )
            )
            IconButton(
                modifier = Modifier.padding(vertical = 5.dp),
                onClick = { sendEvent(Event.UpdateSearchTxt("")) },
                enabled = searchTxt.isNotEmpty()
            ) {
                if (searchTxt.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = "cancel"
                    )
                }
            }
        }
    }
}

//fun <T> debounce(
//    waitMs: Long = 300L,
//    scope: CoroutineScope,
//    destinationFunction: (T) -> Unit
//): (T) -> Unit {
//    var debounceJob: Job? = null
//    return { param: T ->
//        debounceJob?.cancel()
//        debounceJob = scope.launch {
//            delay(waitMs)
//            destinationFunction(param)
//        }
//    }
//}