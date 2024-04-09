package com.ccinc.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccinc.database.models.TagsDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {

    @Query("SELECT * FROM tags")
    suspend fun getTags(): List<TagsDBO>

    @Query("SELECT * FROM tags")
    fun observeTags(): Flow<List<TagsDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(value: List<TagsDBO>)

    @Delete
    suspend fun remove(value: TagsDBO)

    @Query("DELETE FROM tags")
    suspend fun clean()

}