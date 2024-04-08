package com.ccinc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ccinc.database.dao.BasketDao
import com.ccinc.database.dao.CategoriesDao
import com.ccinc.database.dao.ProductsDao
import com.ccinc.database.dao.TagsDao
import com.ccinc.database.models.ArrayListConverter
import com.ccinc.database.models.BasketDBO
import com.ccinc.database.models.CategoriesDBO
import com.ccinc.database.models.ProductsDBO
import com.ccinc.database.models.TagsDBO

class FoodDatabase internal constructor(private val database: RoomFoodDatabase) {

    val categoriesDao: CategoriesDao
        get() = database.categoriesDao()

    val productsDao: ProductsDao
        get() = database.productsDao()

    val tagsDao: TagsDao
        get() = database.tagsDao()

    val basketDao: BasketDao
        get() = database.basketDao()

}

@Database(
    entities = [CategoriesDBO::class, ProductsDBO::class, TagsDBO::class, BasketDBO::class],
    version = 1
)
@TypeConverters(ArrayListConverter::class)
internal abstract class RoomFoodDatabase : RoomDatabase() {

    abstract fun categoriesDao(): CategoriesDao

    abstract fun productsDao(): ProductsDao

    abstract fun tagsDao(): TagsDao

    abstract fun basketDao(): BasketDao
}

fun foodDatabase(applicationContext: Context): FoodDatabase {
    val roomFoodDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        RoomFoodDatabase::class.java,
        "test",
    ).build()
    return FoodDatabase(roomFoodDatabase)
}