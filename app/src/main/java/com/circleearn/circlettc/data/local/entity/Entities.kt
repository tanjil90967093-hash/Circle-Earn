package com.circleearn.circlettc.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val role: String = "USER", // "USER", "ADMIN"
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "wallets",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WalletEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val availableBalance: Double = 0.0,
    val earningsBalance: Double = 0.0,
    val depositBalance: Double = 0.0,
    val pendingBalance: Double = 0.0
)

@Entity(
    tableName = "wallet_transactions",
    foreignKeys = [
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["id"],
            childColumns = ["walletId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val walletId: String,
    val amount: Double,
    val type: String, // "DEPOSIT", "WITHDRAWAL", "JOB_REWARD", "JOB_FEE", "TOPUP"
    val status: String, // "PENDING", "COMPLETED", "FAILED"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconName: String
)

@Entity(
    tableName = "jobs",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["posterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class JobEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val posterId: String,
    val categoryId: String,
    val title: String,
    val description: String,
    val rewardPerWorker: Double,
    val requiredWorkers: Int,
    val completedWorkers: Int = 0,
    val status: String = "AVAILABLE", // "AVAILABLE", "COMPLETED", "CANCELLED"
    val deadline: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "job_assignments",
    foreignKeys = [
        ForeignKey(
            entity = JobEntity::class,
            parentColumns = ["id"],
            childColumns = ["jobId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["workerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class JobAssignmentEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val jobId: String,
    val workerId: String,
    val status: String = "ACCEPTED", // "ACCEPTED", "SUBMITTED", "UNDER_REVIEW", "APPROVED", "REJECTED"
    val assignedAt: Long = System.currentTimeMillis()
)
