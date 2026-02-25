package com.example.job_portal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.JobModel
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
    val context = LocalContext.current
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
                    JobItemCard(
                        job = job,
                        onDelete = { jobId ->
                            jobViewModel.deleteJob(jobId) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        },
                        onEdit = { selectedJob ->
                            val intent = Intent(context, EditJobActivity::class.java).apply {
                                putExtra("jobId", selectedJob.jobId)
                                putExtra("title", selectedJob.title)
                                putExtra("company", selectedJob.company)
                                putExtra("location", selectedJob.location)
                                putExtra("salary", selectedJob.salary)
                                putExtra("type", selectedJob.type)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun JobItemCard(
    job: JobModel,
    onDelete: (String) -> Unit,
    onEdit: (JobModel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
                }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = CoffeeBrown
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                expanded = false
                                onEdit(job)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = Color.Red) },
                            onClick = {
                                expanded = false
                                onDelete(job.jobId)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(12.dp))

            // Details with Labels
            JobDetailRow(label = "Location", value = job.location)
            JobDetailRow(label = "Salary", value = job.salary)
            JobDetailRow(label = "Job Type", value = job.type)

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

@Composable
fun JobDetailRow(label: String, value: String) {
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