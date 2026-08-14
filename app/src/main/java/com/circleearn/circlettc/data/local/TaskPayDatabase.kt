package com.circleearn.circlettc.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.circleearn.circlettc.data.local.dao.TaskPayDao
import com.circleearn.circlettc.data.local.entity.CategoryEntity
import com.circleearn.circlettc.data.local.entity.JobAssignmentEntity
import com.circleearn.circlettc.data.local.entity.JobEntity
import com.circleearn.circlettc.data.local.entity.TransactionEntity
import com.circleearn.circlettc.data.local.entity.UserEntity
import com.circleearn.circlettc.data.local.entity.WalletEntity

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
