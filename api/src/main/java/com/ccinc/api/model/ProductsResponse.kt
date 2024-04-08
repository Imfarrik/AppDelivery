package com.ccinc.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    @SerialName("carbohydrates_per_100_grams")
    val carbohydratesPer100Grams: Double?,
    @SerialName("category_id")
    val categoryId: Long?,
    @SerialName("description")
    val description: String?,
    @SerialName("energy_per_100_grams")
    val energyPer100Grams: Double?,
    @SerialName("fats_per_100_grams")
    val fatsPer100Grams: Double?,
    @SerialName("id")
    val id: Long,
    @SerialName("image")
    val image: String?,
    @SerialName("measure")
    val measure: Long?,
    @SerialName("measure_unit")
    val measureUnit: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("price_current")
    val priceCurrent: Long?,
    @SerialName("price_old")
    val priceOld: Long?,
    @SerialName("proteins_per_100_grams")
    val proteinsPer100Grams: Double?,
    @SerialName("tag_ids")
    val tagIds: List<Long>
)