package com.ccinc.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccinc.database.models.BasketDBO
import com.ccinc.database.models.CategoriesDBO
import com.ccinc.database.models.ProductsDBO
import com.ccinc.database.models.TagsDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {

    @Query("SELECT * FROM basket")
    suspend fun getBasket(): List<BasketDBO>

    @Query("SELECT * FROM basket")
    fun observeBasket(): Flow<List<BasketDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(value: BasketDBO)

    @Delete
    suspend fun remove(value: BasketDBO)

    @Query("DELETE FROM basket")
    suspend fun clean()

}