package com.example.job_portal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag // Required for identification in tests
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// UI Theme Imports
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.example.job_portal.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(jobViewModel: JobViewModel, userViewModel: UserViewModel) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Observe Job data and User profile data
    val jobsFromDb by jobViewModel.allJobs.observeAsState(initial = emptyList())
    val userData by userViewModel.users.observeAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        jobViewModel.fetchAllJobs()
        jobViewModel.fetchUserApplications()
        if (userId.isNotEmpty()) {
            jobViewModel.fetchSavedJobs(userId)
            userViewModel.getUserById(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search Header
        Text(
            text = "Explore Jobs",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryIndigo,
            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
        )

        // UPDATED: Added testTag to the Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .testTag("search_field"), // Tag for identification in UI tests
            placeholder = { Text("Search by title or company...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = PrimaryIndigo
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedBorderColor = PrimaryIndigo,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = PrimaryIndigo,
                focusedLabelColor = PrimaryIndigo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        val filteredJobs = jobsFromDb.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.company.contains(searchQuery, ignoreCase = true)
        }

        if (filteredJobs.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("no_jobs_found_box"), // Tag for checking empty states
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "No jobs found for '$searchQuery'",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
            }
        }

        // UPDATED: Added testTag to the list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("filtered_job_list"), // Tag to verify the list exists
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
        ) {
            items(filteredJobs, key = { it.jobId }) { job ->
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