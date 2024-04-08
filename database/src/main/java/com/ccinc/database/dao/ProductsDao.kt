package com.ccinc.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccinc.database.models.CategoriesDBO
import com.ccinc.database.models.ProductsDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<ProductsDBO>

    @Query("SELECT * FROM products")
    fun observeProducts(): Flow<List<ProductsDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(value: List<ProductsDBO>)

    @Delete
    suspend fun remove(value: ProductsDBO)

    @Query("DELETE FROM products")
    suspend fun clean()

}