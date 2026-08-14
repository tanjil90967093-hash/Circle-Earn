package com.circleearn.circlettc

import android.app.Application
import com.circleearn.circlettc.data.local.TaskPayDatabase
import com.circleearn.circlettc.data.repository.TaskPayRepository

class TaskPayApplication : Application() {
    val database by lazy { TaskPayDatabase.getDatabase(this) }
    val repository by lazy { TaskPayRepository(database.taskPayDao()) }
}
