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
import com.example.job_portal.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SavedJobsScreen(jobViewModel: JobViewModel, userViewModel: UserViewModel) {
    val allJobs by jobViewModel.allJobs.observeAsState(initial = emptyList())
    val userData by userViewModel.users.observeAsState()
    val savedIds = jobViewModel.savedJobIds
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val savedJobsList = allJobs.filter { job -> savedIds.contains(job.jobId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(16.dp)
    ) {
        Text("Your Saved Jobs", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CoffeeBrown)

        if (savedJobsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No saved jobs yet!", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(savedJobsList) { job ->
                    // FIXED: Now passing all 4 required arguments
                    JobItemCard(
                        job = job,
                        viewModel = jobViewModel,
                        userId = userId,
                        userData = userData
                    )
                }
            }
        }
    }
}