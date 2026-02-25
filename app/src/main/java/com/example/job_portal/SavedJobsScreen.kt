package com.example.job_portal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.viewmodel.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SavedJobsScreen(viewModel: JobViewModel) {
    // 1. Get all jobs and the list of saved IDs
    val allJobs by viewModel.allJobs.observeAsState(initial = emptyList())
    val savedIds = viewModel.savedJobIds
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // 2. Filter the jobs to only show the ones whose ID is in the savedIds list
    val savedJobsList = allJobs.filter { job -> savedIds.contains(job.jobId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(16.dp)
    ) {
        Text(
            text = "Your Saved Jobs",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (savedJobsList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No saved jobs yet!",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Jobs you save will appear here.",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(savedJobsList) { job ->
                    // 3. Pass the userId here to match the new JobItemCard signature
                    JobItemCard(job = job, viewModel = viewModel, userId = userId)
                }
            }
        }
    }
}