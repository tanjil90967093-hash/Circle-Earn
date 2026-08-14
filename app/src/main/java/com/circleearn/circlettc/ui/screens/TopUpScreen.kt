package com.circleearn.circlettc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.circleearn.circlettc.ui.viewmodel.TaskPayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUpScreen(viewModel: TaskPayViewModel) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background, 
        topBar = {
            TopAppBar(title = { Text("Game Top-Up", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Free Fire Diamonds", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Enter your Player ID and select a package to top up.")
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Player ID / UID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { viewModel.topUp("Free Fire", "uid_123", 100.0, 80.0) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Buy 100 Diamonds (৳80.00)")
                        }
                    }
                }
            }
        }
    }
}
