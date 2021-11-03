package com.smaher.tasktimerapp_pinkninjas.database

import androidx.lifecycle.LiveData

class TaskRepository(private val dao:TaskDao){

    val getTasks: LiveData<List<Task>> = dao.getTasks()

    suspend fun insert(task: Task){
        dao.addTask(task)
    }

    suspend fun update(task: Task){
        dao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        dao.deleteTask(task)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }

}