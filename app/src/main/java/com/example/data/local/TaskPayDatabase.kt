package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.TaskPayDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.JobAssignmentEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WalletEntity

@Database(
    entities = [
        UserEntity::class,
        WalletEntity::class,
        TransactionEntity::class,
        CategoryEntity::class,
        JobEntity::class,
        JobAssignmentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TaskPayDatabase : RoomDatabase() {

    abstract fun taskPayDao(): TaskPayDao

    companion object {
        @Volatile
        private var INSTANCE: TaskPayDatabase? = null

        fun getDatabase(context: Context): TaskPayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskPayDatabase::class.java,
                    "taskpay_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
