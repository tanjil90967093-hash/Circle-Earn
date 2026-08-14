package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.JobAssignmentEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskPayDao {

    // --- Users ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserFlow(userId: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUser(userId: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    // --- Wallets ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: WalletEntity)

    @Query("SELECT * FROM wallets WHERE userId = :userId")
    fun getWalletFlow(userId: String): Flow<WalletEntity?>
    
    @Query("SELECT * FROM wallets WHERE userId = :userId")
    suspend fun getWallet(userId: String): WalletEntity?

    @Update
    suspend fun updateWallet(wallet: WalletEntity)

    // --- Transactions ---
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM wallet_transactions WHERE walletId = :walletId ORDER BY timestamp DESC")
    fun getTransactionsFlow(walletId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    // --- Categories ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories")
    fun getCategoriesFlow(): Flow<List<CategoryEntity>>

    // --- Jobs ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    @Update
    suspend fun updateJob(job: JobEntity)

    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    fun getAllJobsFlow(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE status = :status ORDER BY createdAt DESC")
    fun getJobsByStatusFlow(status: String): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :jobId")
    suspend fun getJob(jobId: String): JobEntity?

    // --- Job Assignments ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobAssignment(assignment: JobAssignmentEntity)

    @Update
    suspend fun updateJobAssignment(assignment: JobAssignmentEntity)

    @Query("SELECT * FROM job_assignments WHERE workerId = :userId ORDER BY assignedAt DESC")
    fun getJobAssignmentsForUserFlow(userId: String): Flow<List<JobAssignmentEntity>>

    @Query("SELECT * FROM job_assignments WHERE jobId = :jobId")
    fun getAssignmentsForJobFlow(jobId: String): Flow<List<JobAssignmentEntity>>

    @Query("SELECT COUNT(*) FROM job_assignments WHERE jobId = :jobId AND workerId = :userId")
    suspend fun hasUserAcceptedJob(jobId: String, userId: String): Int
}
