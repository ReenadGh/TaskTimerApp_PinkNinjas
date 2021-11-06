package com.smaher.tasktimerapp_pinkninjas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.database.TaskDatabase
import com.smaher.tasktimerapp_pinkninjas.database.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel (application: Application) : AndroidViewModel(application){
    val tasks: LiveData<List<Task>>
    val repository:TaskRepository
    var task : LiveData<Task>? = null

    init{
        val dao = TaskDatabase.getInstance(application).taskDao()
        repository = TaskRepository(dao)
        tasks = repository.getTasks
    }

    fun deleteTask(task: Task) = viewModelScope.launch( Dispatchers.IO ){
        repository.delete(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch( Dispatchers.IO ){
        repository.update(task)
    }

    fun addTask(task: Task) = viewModelScope.launch( Dispatchers.IO ){
        repository.insert(task)
    }

    fun deleteAll() = viewModelScope.launch( Dispatchers.IO ){
        repository.deleteAll()
    }

}