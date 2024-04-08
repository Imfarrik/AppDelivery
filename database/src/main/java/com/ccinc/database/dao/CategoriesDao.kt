package com.ccinc.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccinc.database.models.CategoriesDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoriesDBO>

    @Query("SELECT * FROM categories")
    fun observeCategories(): Flow<List<CategoriesDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(value: List<CategoriesDBO>)

    @Delete
    suspend fun remove(value: CategoriesDBO)

    @Query("DELETE FROM categories")
    suspend fun clean()

}