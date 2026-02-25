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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(jobViewModel: JobViewModel) {
    // 1. Get current userId to handle saving persistence
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Observe the live data from the shared ViewModel
    val jobsFromDb by jobViewModel.allJobs.observeAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }

    // Fetch all necessary data when screen launches
    LaunchedEffect(Unit) {
        jobViewModel.fetchAllJobs()
        // Critical: fetch applications so we know which buttons to disable
        jobViewModel.fetchUserApplications()
        if (userId.isNotEmpty()) {
            jobViewModel.fetchSavedJobs(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(16.dp)
    ) {
        // Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for your dream job...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CoffeeBrown) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = White,
                focusedIndicatorColor = CoffeeBrown,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = CoffeeBrown
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Results List
        val filteredJobs = jobsFromDb.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.company.contains(searchQuery, ignoreCase = true)
        }

        if (filteredJobs.isEmpty() && searchQuery.isNotEmpty()) {
            Text(
                text = "No jobs found for '$searchQuery'",
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredJobs, key = { it.jobId }) { job ->
                // This JobItemCard is the same one used in HomeScreen
                // It now contains the "Applied" check logic internally
                JobItemCard(
                    job = job,
                    viewModel = jobViewModel,
                    userId = userId
                )
            }
        }
    }
}