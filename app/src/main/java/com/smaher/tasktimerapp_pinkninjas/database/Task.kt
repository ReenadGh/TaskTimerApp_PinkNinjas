package com.smaher.tasktimerapp_pinkninjas.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// this data class will be modified
@Entity(tableName = "Tasks")
data class Task(
               @PrimaryKey(autoGenerate = true)
                val id: Int,
                var name:String,
                var description:String,
                var totalTime:Int) {
}