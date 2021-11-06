package com.smaher.tasktimerapp_pinkninjas.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


// this data class will be modified
@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name:String,
    var description:String,
    var image: Int? =null,
    var status:String = "new",
    var totalTime:Long,
    var currentTime:Long = 0L

    ):Serializable