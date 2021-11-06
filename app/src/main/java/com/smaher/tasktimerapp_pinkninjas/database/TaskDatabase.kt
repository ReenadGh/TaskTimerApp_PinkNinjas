package com.smaher.tasktimerapp_pinkninjas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class],version = 1,exportSchema = false)
abstract class TaskDatabase: RoomDatabase()  {

    companion object{
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context):TaskDatabase {
            if (INSTANCE != null) {
                return INSTANCE as TaskDatabase
            }
            synchronized(this){  //for the protection purpose from concurrent execution on multi threading
                val instance = Room.databaseBuilder(context.applicationContext, TaskDatabase::class.java, "tasks"
                ).fallbackToDestructiveMigration()  // Destroys old database on version change
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
    abstract fun taskDao(): TaskDao
}