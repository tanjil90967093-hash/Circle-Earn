package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.TaskPayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(viewModel: TaskPayViewModel) {
    val jobs by viewModel.availableJobs.collectAsState()
    
    Scaffold(containerColor = MaterialTheme.colorScheme.background, 
        topBar = {
            TopAppBar(title = { Text("Available Jobs", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (jobs.isEmpty()) {
                item {
                    Text("No jobs available right now.", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(jobs) { job ->
                    JobCard(job = job, onAccept = { viewModel.acceptJob(job.id) })
                }
            }
        }
    }
}
