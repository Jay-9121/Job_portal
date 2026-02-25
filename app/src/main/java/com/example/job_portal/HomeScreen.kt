package com.example.job_portal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel

@Composable
fun HomeScreen() {
    val repo = remember { JobRepoImpl() }
    val jobViewModel = remember { JobViewModel(repo) }
    val jobsFromDb by jobViewModel.allJobs.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        jobViewModel.fetchAllJobs()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(16.dp)
    ) {
        Text(
            text = "Popular Opportunities",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (jobsFromDb.isNullOrEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 50.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = CoffeeBrown)
                    }
                }
            } else {
                items(jobsFromDb!!) { job ->
                    JobItemCard(job)
                }
            }
        }
    }
}

@Composable
fun JobItemCard(job: JobModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Title and Company Header
            Text(
                text = job.title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = CoffeeBrown
            )
            Text(
                text = job.company,
                color = Color.Gray,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(12.dp))

            // Using the UNIQUE helper function name to avoid conflicts
            HomeJobDetailRow(label = "Location", value = job.location)
            HomeJobDetailRow(label = "Salary", value = job.salary)
            HomeJobDetailRow(label = "Job Type", value = job.type)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Application Logic */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Apply Now", color = White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// UNIQUE NAME: This avoids the "Conflicting overloads" error
@Composable
fun HomeJobDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    }
}