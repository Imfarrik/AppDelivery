package com.ccinc.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket")
data class BasketDBO(
    @ColumnInfo("carbohydrates_per_100_grams")
    val carbohydratesPer100Grams: Double?,
    @ColumnInfo("category_id")
    val categoryId: Long?,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("energy_per_100_grams")
    val energyPer100Grams: Double?,
    @ColumnInfo("fats_per_100_grams")
    val fatsPer100Grams: Double?,
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("image")
    val image: String?,
    @ColumnInfo("measure")
    val measure: Long?,
    @ColumnInfo("measure_unit")
    val measureUnit: String?,
    @ColumnInfo("name")
    val name: String?,
    @ColumnInfo("price_current")
    val priceCurrent: Long?,
    @ColumnInfo("price_old")
    val priceOld: Long?,
    @ColumnInfo("proteins_per_100_grams")
    val proteinsPer100Grams: Double?,
    @ColumnInfo("tag_ids")
    val tagIds: List<Long>,
    @ColumnInfo("count")
    val count: Int
)

