package com.example.job_portal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdminDashboardScreen(onAddClick = {
                startActivity(Intent(this, AddJobActivity::class.java))
            })
        }
    }
}

@Composable
fun AdminDashboardScreen(onAddClick: () -> Unit) {
    val repo = remember { JobRepoImpl() }
    val jobViewModel = remember { JobViewModel(repo) }
    val jobsFromDb by jobViewModel.allJobs.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        jobViewModel.fetchAllJobs()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = CoffeeBrown,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Job")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(SoftCream)
                .padding(16.dp)
        ) {
            Text(
                text = "Admin Panel: All Jobs",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CoffeeBrown
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(jobsFromDb ?: emptyList()) { job ->
                    // Reusing your JobItemCard here
                    JobItemCard(job)
                }
            }
        }
    }
}