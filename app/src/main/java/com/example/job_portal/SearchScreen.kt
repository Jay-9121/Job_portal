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

@Composable
fun SearchScreen(jobViewModel: JobViewModel) {
    // 1. Get current userId to handle saving persistence
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Observe the live data from the shared ViewModel
    val jobsFromDb by jobViewModel.allJobs.observeAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }

    // Fetch jobs and saved status when the screen is first launched
    LaunchedEffect(Unit) {
        jobViewModel.fetchAllJobs()
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
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = White,
                focusedIndicatorColor = CoffeeBrown,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Results List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Filter logic based on search query
            val filteredJobs = jobsFromDb.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.company.contains(searchQuery, ignoreCase = true)
            }

            items(filteredJobs) { job ->
                // 2. FIXED: Passed the userId to the JobItemCard
                JobItemCard(
                    job = job,
                    viewModel = jobViewModel,
                    userId = userId
                )
            }
        }
    }
}