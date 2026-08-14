package com.example

import android.app.Application
import com.example.data.local.TaskPayDatabase
import com.example.data.repository.TaskPayRepository

class TaskPayApplication : Application() {
    val database by lazy { TaskPayDatabase.getDatabase(this) }
    val repository by lazy { TaskPayRepository(database.taskPayDao()) }
}
