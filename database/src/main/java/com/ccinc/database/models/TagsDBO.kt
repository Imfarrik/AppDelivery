package com.ccinc.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagsDBO(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("name")
    val name: String
)