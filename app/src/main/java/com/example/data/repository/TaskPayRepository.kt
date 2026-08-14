package com.example.data.repository

import com.example.data.local.dao.TaskPayDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.JobAssignmentEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WalletEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

class TaskPayRepository(private val dao: TaskPayDao) {

    // --- Users & Setup ---
    suspend fun setupMockData() = withContext(Dispatchers.IO) {
        val adminId = "admin_123"
        val existingAdmin = dao.getUser(adminId)
        if (existingAdmin == null) {
            val adminUser = UserEntity(id = adminId, name = "Admin User", email = "admin@taskpay.com", role = "ADMIN")
            val adminWallet = WalletEntity(id = UUID.randomUUID().toString(), userId = adminId)
            dao.insertUser(adminUser)
            dao.insertWallet(adminWallet)

            val testUser = UserEntity(id = "user_123", name = "Test Worker", email = "worker@taskpay.com", role = "USER")
            val testWallet = WalletEntity(id = UUID.randomUUID().toString(), userId = "user_123", availableBalance = 50.0, depositBalance = 50.0)
            dao.insertUser(testUser)
            dao.insertWallet(testWallet)
            
            dao.insertTransaction(TransactionEntity(
                walletId = testWallet.id,
                amount = 50.0,
                type = "DEPOSIT",
                status = "COMPLETED"
            ))

            // Categories
            dao.insertCategory(CategoryEntity(name = "Micro Jobs", iconName = "Work"))
            val surveyCategory = CategoryEntity(name = "Survey", iconName = "List")
            dao.insertCategory(surveyCategory)

            // Jobs
            dao.insertJob(JobEntity(
                posterId = adminId,
                categoryId = surveyCategory.id,
                title = "Complete User Feedback Survey",
                description = "Answer 10 simple questions about your daily app usage.",
                rewardPerWorker = 5.0,
                requiredWorkers = 10,
                deadline = System.currentTimeMillis() + 86400000 // 1 day
            ))
        }
    }

    fun getCurrentUserFlow(userId: String) = dao.getUserFlow(userId)
    fun getWalletFlow(userId: String) = dao.getWalletFlow(userId)
    fun getTransactionsFlow(walletId: String) = dao.getTransactionsFlow(walletId)
    fun getCategoriesFlow() = dao.getCategoriesFlow()
    fun getAvailableJobsFlow() = dao.getJobsByStatusFlow("AVAILABLE")
    fun getAssignmentsForUserFlow(userId: String) = dao.getJobAssignmentsForUserFlow(userId)

    // --- Business Logic / Ledger Simulation ---

    suspend fun deposit(userId: String, amount: Double): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val wallet = dao.getWallet(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))
            
            val updatedWallet = wallet.copy(
                availableBalance = wallet.availableBalance + amount,
                depositBalance = wallet.depositBalance + amount
            )
            dao.updateWallet(updatedWallet)
            
            dao.insertTransaction(TransactionEntity(
                walletId = updatedWallet.id,
                amount = amount,
                type = "DEPOSIT",
                status = "COMPLETED"
            ))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun withdraw(userId: String, amount: Double): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val wallet = dao.getWallet(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))
            if (wallet.availableBalance < amount) {
                return@withContext Result.failure(Exception("Insufficient balance"))
            }

            val updatedWallet = wallet.copy(
                availableBalance = wallet.availableBalance - amount,
                pendingBalance = wallet.pendingBalance + amount
            )
            dao.updateWallet(updatedWallet)
            
            dao.insertTransaction(TransactionEntity(
                walletId = updatedWallet.id,
                amount = -amount,
                type = "WITHDRAWAL",
                status = "PENDING"
            ))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptJob(userId: String, jobId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val job = dao.getJob(jobId) ?: return@withContext Result.failure(Exception("Job not found"))
            if (job.status != "AVAILABLE" || job.completedWorkers >= job.requiredWorkers) {
                return@withContext Result.failure(Exception("Job no longer available"))
            }
            if (dao.hasUserAcceptedJob(jobId, userId) > 0) {
                return@withContext Result.failure(Exception("You have already accepted this job"))
            }

            dao.insertJobAssignment(JobAssignmentEntity(
                jobId = jobId,
                workerId = userId,
                status = "ACCEPTED"
            ))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitJobProof(assignmentId: String): Result<Unit> = withContext(Dispatchers.IO) {
        // Mocking: Just updating the assignment in a real app, this would upload an image.
        Result.success(Unit)
    }

    suspend fun topUpGame(userId: String, game: String, playerId: String, amount: Double, price: Double): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val wallet = dao.getWallet(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))
            if (wallet.availableBalance < price) {
                return@withContext Result.failure(Exception("Insufficient balance"))
            }

            val updatedWallet = wallet.copy(
                availableBalance = wallet.availableBalance - price
            )
            dao.updateWallet(updatedWallet)
            
            dao.insertTransaction(TransactionEntity(
                walletId = updatedWallet.id,
                amount = -price,
                type = "TOPUP",
                status = "COMPLETED" // Mocking instant success
            ))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
