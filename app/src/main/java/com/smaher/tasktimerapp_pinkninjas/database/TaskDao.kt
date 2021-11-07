package com.smaher.tasktimerapp_pinkninjas.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Query("SELECT * FROM Tasks ORDER BY id DESC")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM Tasks WHERE id =:id")
    fun getOneTask(id :Int): Task

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM Tasks")
    suspend fun deleteAll() : Int


}