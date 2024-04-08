package com.ccinc.data.utils

import com.ccinc.api.model.CategoriesResponse
import com.ccinc.api.model.ProductsResponse
import com.ccinc.api.model.TagsResponse
import com.ccinc.data.model.Basket
import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products
import com.ccinc.data.model.Tags
import com.ccinc.database.models.BasketDBO
import com.ccinc.database.models.CategoriesDBO
import com.ccinc.database.models.ProductsDBO
import com.ccinc.database.models.TagsDBO

/**
 * Categories
 */

internal fun CategoriesDBO.toCategories(): Categories {
    return Categories(
        id = id,
        name = name
    )
}

internal fun CategoriesResponse.toCategories(): Categories {
    return Categories(
        id = id,
        name = name
    )
}

internal fun CategoriesResponse.toCategoriesDBO(): CategoriesDBO {
    return CategoriesDBO(
        id = id,
        name = name
    )
}

/**
 * Tags
 */

internal fun TagsDBO.toTags(): Tags {
    return Tags(
        id = id,
        name = name
    )
}

internal fun TagsResponse.toTags(): Tags {
    return Tags(
        id = id,
        name = name
    )
}

internal fun TagsResponse.toTagsDBO(): TagsDBO {
    return TagsDBO(
        id = id,
        name = name
    )
}

/**
 * Products
 */

internal fun ProductsDBO.toProduct(): Products {
    return Products(
        id = id,
        name = name,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        categoryId = categoryId,
        description = description,
        energyPer100Grams = energyPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        image = image,
        measure = measure,
        measureUnit = measureUnit,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        proteinsPer100Grams = proteinsPer100Grams,
        tagIds = tagIds
    )
}

fun Products.toBasket(count: Int = 1): Basket {
    return Basket(
        id = id,
        name = name,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        categoryId = categoryId,
        description = description,
        energyPer100Grams = energyPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        image = image,
        measure = measure,
        measureUnit = measureUnit,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        proteinsPer100Grams = proteinsPer100Grams,
        tagIds = tagIds,
        count = count
    )
}

internal fun ProductsResponse.toProduct(): Products {
    return Products(
        id = id,
        name = name,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        categoryId = categoryId,
        description = description,
        energyPer100Grams = energyPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        image = image,
        measure = measure,
        measureUnit = measureUnit,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        proteinsPer100Grams = proteinsPer100Grams,
        tagIds = tagIds
    )
}

internal fun ProductsResponse.toProductsDBO(): ProductsDBO {
    return ProductsDBO(
        id = id,
        name = name,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        categoryId = categoryId,
        description = description,
        energyPer100Grams = energyPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        image = image,
        measure = measure,
        measureUnit = measureUnit,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        proteinsPer100Grams = proteinsPer100Grams,
        tagIds = tagIds
    )
}

/**
 * Basket
 */

internal fun Basket.toBasketDBO(): BasketDBO {
    return BasketDBO(
        id = id,
        name = name,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        categoryId = categoryId,
        description = description,
        energyPer100Grams = energyPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        image = image,
        measure = measure,
        measureUnit = measureUnit,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        proteinsPer100Grams = proteinsPer100Grams,
        tagIds = tagIds,
        count = count
    )
}

internal fun BasketDBO.toBasket(): Basket {
    return Basket(
        id = id,
        name = name,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        categoryId = categoryId,
        description = description,
        energyPer100Grams = energyPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        image = image,
        measure = measure,
        measureUnit = measureUnit,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        proteinsPer100Grams = proteinsPer100Grams,
        tagIds = tagIds,
        count = count
    )
}

internal fun Products.toProductDBO(): ProductsDBO {
    return ProductsDBO(
        id = id,
        name = name,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        categoryId = categoryId,
        description = description,
        energyPer100Grams = energyPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        image = image,
        measure = measure,
        measureUnit = measureUnit,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        proteinsPer100Grams = proteinsPer100Grams,
        tagIds = tagIds
    )
}