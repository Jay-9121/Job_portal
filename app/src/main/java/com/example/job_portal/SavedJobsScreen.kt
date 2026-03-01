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
// UI Theme Imports - Updated to the modern palette
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.example.job_portal.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SavedJobsScreen(jobViewModel: JobViewModel, userViewModel: UserViewModel) {
    val allJobs by jobViewModel.allJobs.observeAsState(initial = emptyList())
    val userData by userViewModel.users.observeAsState()
    val savedIds = jobViewModel.savedJobIds
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Filter jobs based on whether their ID is in the saved list
    val savedJobsList = allJobs.filter { job -> savedIds.contains(job.jobId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray) // Updated from SoftCream
            .padding(16.dp)
    ) {
        // Updated Header with Indigo theme
        Text(
            text = "Your Saved Jobs",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryIndigo // Updated from CoffeeBrown
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (savedJobsList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.baseline_bookmark_border_24),
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No saved jobs yet!",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp) // Extra padding for bottom nav
            ) {
                items(savedJobsList, key = { it.jobId }) { job ->
                    // Passing all required arguments to JobItemCard
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