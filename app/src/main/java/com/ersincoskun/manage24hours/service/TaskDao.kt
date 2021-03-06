package com.ersincoskun.manage24hours.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ersincoskun.manage24hours.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task):Long

    @Query("SELECT * FROM tasks")
    suspend fun getAllTask():List<Task>

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}