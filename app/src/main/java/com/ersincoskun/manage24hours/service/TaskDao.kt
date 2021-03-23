package com.ersincoskun.manage24hours.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ersincoskun.manage24hours.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task): Long

    @Insert
    suspend fun insertAll(vararg tasks: Task): List<Long>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTask(): MutableList<Task>

    @Query("SELECT * FROM tasks WHERE uuid=:taskId")
    suspend fun getTask(taskId: Long): Task

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

}