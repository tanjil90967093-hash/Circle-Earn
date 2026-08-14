package com.circleearn.circlettc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.circleearn.circlettc.data.local.entity.CategoryEntity
import com.circleearn.circlettc.data.local.entity.JobEntity
import com.circleearn.circlettc.data.local.entity.WalletEntity
import com.circleearn.circlettc.ui.theme.BannerGradientEnd
import com.circleearn.circlettc.ui.theme.BannerGradientStart
import com.circleearn.circlettc.ui.theme.TextPrimary
import com.circleearn.circlettc.ui.viewmodel.TaskPayViewModel

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: TaskPayViewModel) {
    val wallet by viewModel.wallet.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val jobs by viewModel.availableJobs.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccountBalanceWallet,
                            contentDescription = "Logo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("TaskPay", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
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
                HomeBanner()
            }
            
            item {
                WalletSummaryCard(wallet)
            }
            
            item {
                Text("Job Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            
            items(categories) { category ->
                CategoryCard(category)
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Featured Micro Jobs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            
            items(jobs.take(3)) { job ->
                JobCard(job, onAccept = { viewModel.acceptJob(job.id) })
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

data class BannerData(
    val title: String,
    val subtitle: String,
    val buttonText: String,
    val gradientStart: Color,
    val gradientEnd: Color
)

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HomeBanner() {
    // In a real app, this list would be fetched from the backend via the ViewModel
    val banners = listOf(
        BannerData(
            "Earn Money Daily!",
            "Complete simple micro jobs and get paid instantly to your wallet.",
            "Explore",
            BannerGradientStart,
            BannerGradientEnd
        ),
        BannerData(
            "Refer & Earn",
            "Invite friends and earn ৳50 for each successful signup.",
            "Invite Now",
            Color(0xFFFF5722),
            BannerGradientStart
        ),
        BannerData(
            "New Gaming Tasks",
            "Top up games or play and earn big rewards today.",
            "Explore",
            Color(0xFF4CAF50),
            BannerGradientEnd
        )
    )

    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { banners.size })
    
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(3000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % pagerState.pageCount
            )
        }
    }

    Column {
        androidx.compose.foundation.pager.HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val banner = banners[page]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(banner.gradientStart, banner.gradientEnd)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        banner.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        banner.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TextPrimary)
                    ) {
                        Text(banner.buttonText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun WalletSummaryCard(wallet: WalletEntity?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Available Balance", style = MaterialTheme.typography.labelMedium)
            Text(
                "৳ ${String.format("%.2f", wallet?.availableBalance ?: 0.0)}", 
                style = MaterialTheme.typography.headlineLarge, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("Earnings", style = MaterialTheme.typography.labelSmall)
                    Text("৳ ${String.format("%.2f", wallet?.earningsBalance ?: 0.0)}", fontWeight = FontWeight.Medium)
                }
                Column {
                    Text("Deposit", style = MaterialTheme.typography.labelSmall)
                    Text("৳ ${String.format("%.2f", wallet?.depositBalance ?: 0.0)}", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: CategoryEntity) {
    Card(
        onClick = { /* TODO */ },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(category.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Earn by completing tasks", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun JobCard(job: JobEntity, onAccept: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(job.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text("৳ ${job.rewardPerWorker}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(job.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Workers: ${job.completedWorkers}/${job.requiredWorkers}", style = MaterialTheme.typography.labelSmall)
                Button(onClick = onAccept) {
                    Text("Accept Job")
                }
            }
        }
    }
}
