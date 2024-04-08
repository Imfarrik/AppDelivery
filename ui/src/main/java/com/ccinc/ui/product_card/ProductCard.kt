package com.ccinc.ui.product_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ccinc.data.model.Products
import com.ccinc.data.model.Tags
import com.ccinc.design_system.Primary
import com.ccinc.ui.R
import com.ccinc.ui.utils.checkForNull
import com.ccinc.ui.utils.toReadableFormat

@Composable
fun ProductCardRoute(products: Products, onBack: () -> Unit) {
    ProductCard(products = products, vm = hiltViewModel(), onBack = onBack)
}

@Composable
private fun ProductCard(
    products: Products,
    vm: ProductCardVM,
    onBack: () -> Unit
) {
    val basket by vm.productsInBasket.collectAsState()
    val isBtnEnabled = basket.find { it.id == products.id } == null
    ProductCardScreen(
        products = products,
        isBtnEnabled = isBtnEnabled,
        sendEvent = vm::sentEvent,
        onBack = onBack
    )
}

@Composable
@Preview
private fun ProductCardScreen(
    products: Products = Products.fake(),
    isBtnEnabled: Boolean = false,
    sendEvent: (Event) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomBar(
                products = products,
                isBtnEnabled = isBtnEnabled,
                sendEvent = sendEvent,
                onBack = onBack
            )
        }
    ) {
        Content(innerPadding = it, products = products, onBack = onBack)
    }
}


@Composable
@Preview
private fun BottomBar(
    products: Products = Products.fake(),
    isBtnEnabled: Boolean = false,
    sendEvent: (Event) -> Unit = {},
    onBack: () -> Unit = {}
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
            onClick = {
                sendEvent(Event.AddProductsInBasket(products))
                onBack()
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            enabled = isBtnEnabled
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.into_basket,
                        products.priceCurrent.toReadableFormat()
                    )
                )
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Content(
    innerPadding: PaddingValues = PaddingValues(),
    products: Products = Products.fake(),
    tags: List<Tags> = listOf(Tags.fake()),
    onBack: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        item {
            Box {
                Box {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.food),
                        contentDescription = "food"
                    )
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomStart)
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        /**
                         * [
                         *     {
                         *         "id": 1,
                         *         "name": "Новинка"
                         *     },
                         *     {
                         *         "id": 2,
                         *         "name": "Вегетарианское блюдо"
                         *     },
                         *     {
                         *         "id": 3,
                         *         "name": "Хит!"
                         *     },
                         *     {
                         *         "id": 4,
                         *         "name": "Острое"
                         *     },
                         *     {
                         *         "id": 5,
                         *         "name": "Экспресс-меню"
                         *     }
                         * ]
                         */


                        /**
                         * Какое АПИ такой и костыль)
                         */

                        val tagIdToDrawable = mapOf(
                            2L to R.drawable.type_veg,
                            4L to R.drawable.type_spyce
                        )

                        products.tagIds.forEach { tagId ->
                            tagIdToDrawable[tagId]?.let { drawableResId ->
                                Image(
                                    modifier = Modifier.size(30.dp),
                                    painter = painterResource(drawableResId),
                                    contentDescription = null
                                )
                            }
                        }

                        products.priceOld?.let {
                            Image(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.tag),
                                contentDescription = null
                            )
                        }

                    }
                }
                Surface(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp),
                    shape = CircleShape,
                    shadowElevation = 2.dp
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = "arrow left"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = products.name.checkForNull(),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W400),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = products.description.checkForNull(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W400,
                        color = SpanStyle().color.copy(alpha = .6f)
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            InfoField(
                stringResource(R.string.weight),
                "${products.measure} ${products.measureUnit}"
            )
            InfoField(
                stringResource(R.string.energy_per_100_grams),
                stringResource(R.string.kkal, products.energyPer100Grams.toString())
            )
            InfoField(
                stringResource(R.string.proteins),
                "${products.proteinsPer100Grams} ${products.measureUnit}"
            )
            InfoField(
                stringResource(R.string.fats),
                "${products.fatsPer100Grams} ${products.measureUnit}"
            )
            InfoField(
                stringResource(R.string.carbohydrates),
                "${products.carbohydratesPer100Grams} ${products.measureUnit}"
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun InfoField(title: String = "Weight", value: String = "400 g") {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W400,
                color = SpanStyle().color.copy(alpha = .6f)
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W400
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}