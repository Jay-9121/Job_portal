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
import com.example.job_portal.ui.theme.CoffeeCream
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
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(job.title, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = CoffeeBrown)
                    Text(job.company, color = Color.Gray, fontSize = 14.sp)
                }
                CircularProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier.size(24.dp),
                    color = CoffeeBrown,
                    strokeWidth = 3.dp,
                    trackColor = CoffeeCream
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(job.location, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("•", color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(job.salary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = CoffeeBrown)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Application Logic */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Apply Now", color = White)
            }
        }
    }
}