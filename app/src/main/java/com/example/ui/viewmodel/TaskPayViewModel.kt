package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.TaskPayRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskPayViewModel(private val repository: TaskPayRepository) : ViewModel() {

    // Simulating a logged-in user session
    val currentUserId = "user_123"

    init {
        viewModelScope.launch {
            repository.setupMockData()
        }
    }

    val currentUser = repository.getCurrentUserFlow(currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val wallet = repository.getWalletFlow(currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val categories = repository.getCategoriesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableJobs = repository.getAvailableJobsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val myAssignments = repository.getAssignmentsForUserFlow(currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deposit(amount: Double) {
        viewModelScope.launch {
            repository.deposit(currentUserId, amount)
        }
    }

    fun withdraw(amount: Double) {
        viewModelScope.launch {
            repository.withdraw(currentUserId, amount)
        }
    }

    fun acceptJob(jobId: String) {
        viewModelScope.launch {
            repository.acceptJob(currentUserId, jobId)
        }
    }
    
    fun topUp(game: String, playerId: String, amount: Double, price: Double) {
        viewModelScope.launch {
            repository.topUpGame(currentUserId, game, playerId, amount, price)
        }
    }
}

class TaskPayViewModelFactory(private val repository: TaskPayRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskPayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskPayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
