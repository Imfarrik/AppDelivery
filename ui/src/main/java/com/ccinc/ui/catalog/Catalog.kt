package com.ccinc.ui.catalog

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ccinc.data.model.Basket
import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products
import com.ccinc.data.model.Tags
import com.ccinc.data.use_cases.CatalogUIModel
import com.ccinc.design_system.GrayBg
import com.ccinc.design_system.Primary
import com.ccinc.ui.R
import com.ccinc.ui.utils.checkForNull
import com.ccinc.ui.utils.toReadableFormat
import kotlinx.coroutines.launch


@Composable
internal fun CatalogRoute(
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
    navigateToSearch: () -> Unit
) {
    Catalog(
        vm = hiltViewModel(),
        navigateToProductCard = navigateToProductCard,
        navigateToBasket = navigateToBasket,
        navigateToSearch = navigateToSearch
    )
}

@Composable
private fun Catalog(
    vm: CatalogVM,
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
    navigateToSearch: () -> Unit
) {
    val state by vm.state.collectAsState()
    val productsInBasket by vm.productsInBasket.collectAsState()

    val currentState = state

    val pin = "2323"
    val token = "farrux"

    var decode: String? = ""
    var encode: String = ""


        encode = CryptoHelper.encrypt(token)
        decode = CryptoHelper.decrypt(encode)


    Box(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = encode, color = Color.Black)
            Text(text = decode.toString(), color = Color.Black)

        }
    }

//    if (currentState !is State.Undefined) {
//        CatalogScreen(
//            currentState = currentState,
//            selectedItem = vm.selectedCategories,
//            productsInBasket = productsInBasket,
//            sentEvent = vm::sentEvent,
//            navigateToProductCard = navigateToProductCard,
//            navigateToBasket = navigateToBasket,
//            navigateToSearch = navigateToSearch,
//            isSheetVisible = vm.isFilterVisible,
//            filterList = vm.filterList
//        )
//    }
}

@Composable
private fun CatalogScreen(
    currentState: State,
    selectedItem: Categories?,
    productsInBasket: List<Basket>,
    filterList: List<Tags>,
    isSheetVisible: Boolean,
    sentEvent: (Event) -> Unit,
    navigateToProductCard: (Products) -> Unit,
    navigateToBasket: () -> Unit,
    navigateToSearch: () -> Unit
) {

    Column {
        if (currentState is State.Error) {
            ErrorMessage(currentState)
        }
        if (currentState is State.Loading) {
            HomeShimmerAnimation()
        }
        if (currentState is State.Success) {
            currentState.data?.let { data ->
                Scaffold(
                    topBar = {
                        TopBar(
                            navigateToSearch = navigateToSearch,
                            sentEvent = sentEvent,
                            filterList = filterList
                        )
                    },
                    containerColor = Color.White,
                    bottomBar = {
                        if (productsInBasket.isNotEmpty()) BottomBar(
                            productsInBasket = productsInBasket,
                            navigateToBasket = navigateToBasket
                        )
                    }
                ) {
                    Column(modifier = Modifier.padding(it)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CategoriesRow(data = data, selectedItem, sentEvent)
                        Spacer(modifier = Modifier.height(16.dp))
                        ProductsRow(
                            data = data,
                            productsInBasket = productsInBasket,
                            sentEvent = sentEvent,
                            filterList = filterList,
                            selectedCategories = selectedItem,
                            navigateToProductCard = navigateToProductCard
                        )
                    }
                }

                FilterSheet(
                    isSheetVisible = isSheetVisible,
                    sendEvent = sentEvent,
                    tags = data.tags ?: listOf(),
                    filterList = filterList
                )
            }
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
    data: CatalogUIModel,
    productsInBasket: List<Basket>,
    sentEvent: (Event) -> Unit,
    filterList: List<Tags> = listOf(),
    selectedCategories: Categories?,
    navigateToProductCard: (Products) -> Unit = {}
) {

    // todo: has to be in vm
    data.products?.let { products ->

        val filteredProductsByCategory = if (selectedCategories == null) {
            products
        } else {
            products.filter { it.categoryId == selectedCategories.id }
        }

        val filteredProductsByFilter = if (filterList.isEmpty()) {
            filteredProductsByCategory
        } else {
            filteredProductsByCategory.filter { it.tagIds.containsAll(filterList.map { t -> t.id }) }
        }

        if (filteredProductsByFilter.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 16.dp),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(items = filteredProductsByFilter, key = { it.id }) {
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
    navigateToProductCard: (Products) -> Unit = { }
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
                        onClick = { sentEvent(Event.DecreaseOrRemoveFromBasket(item)) },
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
                        text = productsInBasket.find { it.id == item.id }!!.count.toString(),
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
private fun CategoriesRow(
    data: CatalogUIModel,
    selectedItem: Categories?,
    sentEvent: (Event) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.categories?.let { categories ->
            items(items = categories, key = { it.id }) { item ->
                CategoriesItem(item, selectedItem, sentEvent)
            }
        }
    }
}

@Composable
@Preview
private fun CategoriesItem(
    item: Categories = Categories.fake(),
    selectedItem: Categories? = Categories.fake(),
    sentEvent: (Event) -> Unit = {}
) {
    FilterChip(
        shape = RoundedCornerShape(8.dp),
        selected = item == selectedItem,
        border = null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Primary,
            selectedLabelColor = Color.White,
            labelColor = Color.Black,
            containerColor = Color.Transparent,
        ),
        onClick = { sentEvent(Event.UpdateSelectedCategories(item)) },
        label = { Text(modifier = Modifier.padding(vertical = 10.dp), text = item.name) })
}

@Composable
@Preview(showBackground = true)
private fun TopBar(
    navigateToSearch: () -> Unit = {},
    sentEvent: (Event) -> Unit = {},
    filterList: List<Tags> = listOf(
        Tags.fake()
    )
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            IconButton(onClick = { sentEvent(Event.IsSheetVisible) }) {
                Image(painter = painterResource(R.drawable.filter), contentDescription = "filter")
            }
            if (filterList.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .aspectRatio(1f)
                        .background(Primary, CircleShape)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = filterList.size.toString(),
                        style = TextStyle(color = Color.White, fontSize = 11.sp)
                    )
                }
            }
        }
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "logo")
        IconButton(onClick = navigateToSearch) {
            Image(painter = painterResource(R.drawable.search), contentDescription = "search")
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FilterSheet(
    isSheetVisible: Boolean,
    sendEvent: (Event) -> Unit,
    tags: List<Tags> = listOf(Tags.fake(), Tags.fake()),
    filterList: List<Tags> = listOf(Tags.fake(), Tags.fake()),
) {

    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { false })
    val scope = rememberCoroutineScope()

    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { sendEvent(Event.IsSheetVisible) },
            sheetState = sheetState,
            containerColor = Color.White,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            properties = ModalBottomSheetDefaults.properties(shouldDismissOnBackPress = true)
        ) {
            SheetContent(
                tags = tags,
                filterList = filterList,
                sendEvent = sendEvent,
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        sendEvent(Event.IsSheetVisible)
                    }
                }
            )
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun SheetContent(
    tags: List<Tags> = listOf(Tags.fake(), Tags.fake()),
    filterList: List<Tags> = listOf(Tags.fake(), Tags.fake()),
    sendEvent: (Event) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.select_eats),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                color = Color.Black.copy(.87f)
            )
        )
        LazyColumn {
            items(tags) { tag ->
                Surface(onClick = { sendEvent(Event.UpdateFilterList(tag)) }, color = Color.White) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 9.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tag.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.W400,
                                fontSize = 16.sp, color = Color.Black.copy(0.87f)
                            )
                        )
                        Checkbox(
                            checked = filterList.contains(tag),
                            onCheckedChange = { sendEvent(Event.UpdateFilterList(tag)) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Primary,
                                uncheckedColor = GrayBg
                            )
                        )
                    }
                }
                if (tag.id != tags.last().id) HorizontalDivider()
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(text = stringResource(R.string.done))
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update", color = MaterialTheme.colorScheme.onError)
    }
}
