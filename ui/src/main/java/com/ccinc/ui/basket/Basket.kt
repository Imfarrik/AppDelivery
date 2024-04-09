package com.ccinc.ui.basket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.ccinc.data.model.Basket
import com.ccinc.design_system.Primary
import com.ccinc.ui.R
import com.ccinc.ui.utils.checkForNull
import com.ccinc.ui.utils.toReadableFormat

@Composable
internal fun BasketRoute(onBack: () -> Unit) {
    Basket(vm = hiltViewModel(), onBack = onBack)
}

@Composable
private fun Basket(vm: BasketVM, onBack: () -> Unit) {
    val products by vm.productsInBasket.collectAsState()
    BasketScreen(onBack = onBack, products = products, sentEvent = vm::sentEvent)
}

@Composable
private fun BasketScreen(
    onBack: () -> Unit,
    products: List<Basket>,
    sentEvent: (Event) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(onBack = onBack)
        },
        bottomBar = {
            BottomBar(products = products)
        }
    ) {
        Content(innerPadding = it, products = products, sentEvent = sentEvent)
    }
}

@Composable
private fun Content(
    innerPadding: PaddingValues = PaddingValues(),
    products: List<Basket> = listOf(
        Basket.fake(),
        Basket.fake(),
        Basket.fake(),
    ),
    sentEvent: (Event) -> Unit = {}
) {
    if (products.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            items(products, key = { it.id }) {

                BasketItem(item = it, productsInBasket = products, sentEvent = sentEvent)

            }

        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.empty_choose_foods_in_catalog),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun BasketItem(
    item: Basket = Basket.fake(),
    productsInBasket: List<Basket> = listOf(),
    sentEvent: (Event) -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
    ) {

        val (image, name, btn, price) = createRefs()

        // Todo: has to be in vm
        var sum = 0L
        productsInBasket.forEach {
            sum += (it.priceCurrent ?: 0) * it.count
        }

        Image(
            modifier = Modifier.constrainAs(image) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            },
            painter = painterResource(id = R.drawable.food),
            contentDescription = "food"
        )

        Text(
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(image.top)
                    bottom.linkTo(btn.top)
                    start.linkTo(image.end, 16.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth(),
            text = item.name.checkForNull(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.W400
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Buttons(
            modifier = Modifier.constrainAs(btn) {
                top.linkTo(name.bottom)
                bottom.linkTo(image.bottom)
                start.linkTo(image.end, 16.dp)
            },
            minusEvent = { sentEvent(Event.DecreaseOrRemoveFromBasket(item)) },
            plusEvent = { sentEvent(Event.AddProductsInBasket(item)) },
            txt = item.count.toString()
        )

        Column(modifier = Modifier.constrainAs(price) {
            top.linkTo(btn.top)
            bottom.linkTo(btn.bottom)
            end.linkTo(parent.end)
            height = Dimension.fillToConstraints
        }, verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "${item.priceCurrent.toReadableFormat()} ₽",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700
                )
            )
            item.priceOld?.let {
                Text(
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
    HorizontalDivider()
}

@Composable
@Preview
private fun Buttons(
    modifier: Modifier = Modifier,
    minusEvent: () -> Unit = {},
    txt: String = "2",
    plusEvent: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Surface(
            onClick = minusEvent,
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
            text = txt,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
        )
        Surface(
            onClick = plusEvent,
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

@Composable
@Preview
private fun BottomBar(products: List<Basket> = listOf(Basket.fake())) {
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
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var sum = 0L
                products.forEach {
                    sum += (it.priceCurrent ?: 0) * it.count
                }
                Text(
                    text = stringResource(
                        R.string.order_for,
                        sum.toReadableFormat()
                    )
                )
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TopBar(onBack: () -> Unit = {}) {
    Surface(color = Color.White, shadowElevation = 12.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(modifier = Modifier.padding(vertical = 5.dp), onClick = onBack) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_left_orange),
                    contentDescription = "arrow_left_orange"
                )
            }
            Text(
                text = stringResource(R.string.basket),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            )
        }
    }
}
