package com.ersincoskun.manage24hours.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "comment")
    val comment: String,
    @ColumnInfo(name = "startTime")
    val startTime: String,
    @ColumnInfo(name = "endTime")
    val endTime: String,
    @ColumnInfo(name = "timeTake")
    val timeTake: String
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Long = 0
}