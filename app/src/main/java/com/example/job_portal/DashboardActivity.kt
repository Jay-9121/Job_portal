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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.ApplicationModel
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = JobRepoImpl()
        val viewModel = JobViewModel(repo)

        setContent {
            var selectedTab by remember { mutableIntStateOf(0) }
            val context = LocalContext.current

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Admin Panel", fontWeight = FontWeight.Bold, color = CoffeeBrown) },
                        actions = {
                            IconButton(onClick = {
                                viewModel.clearAllData()
                                FirebaseAuth.getInstance().signOut()
                                context.startActivity(Intent(context, LoginActivity::class.java))
                                finish()
                            }) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = CoffeeBrown)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
                    )
                },
                bottomBar = {
                    NavigationBar(containerColor = White) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            label = { Text("Jobs") },
                            icon = { Icon(Icons.Default.List, null) }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            label = { Text("Applicants") },
                            icon = { Icon(Icons.Default.Person, null) }
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (selectedTab) {
                        0 -> AdminDashboardScreen(
                            jobViewModel = viewModel,
                            onAddClick = {
                                startActivity(Intent(this@DashboardActivity, AddJobActivity::class.java))
                            }
                        )
                        1 -> AdminApplicationsList(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(jobViewModel: JobViewModel, onAddClick: () -> Unit) {
    val context = LocalContext.current
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
            Text("Manage Job Postings", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CoffeeBrown)
            Spacer(modifier = Modifier.height(16.dp))

            if (jobsFromDb.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No jobs posted yet.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(jobsFromDb) { job ->
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminApplicationsList(viewModel: JobViewModel) {
    // --- FILTER LOGIC ---
    val filterOptions = listOf("All", "Pending", "Accepted", "Declined")
    var selectedFilter by remember { mutableStateOf("All") }

    // Using derivedStateOf to filter the list efficiently
    val filteredApps by remember(selectedFilter, viewModel.allApplications) {
        derivedStateOf {
            val list = viewModel.allApplications.reversed()
            if (selectedFilter == "All") list
            else list.filter { it.status == selectedFilter }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllApplications()
    }

    Column(modifier = Modifier.fillMaxSize().background(SoftCream).padding(16.dp)) {
        Text("Incoming Applications", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CoffeeBrown)

        // --- FILTER CHIPS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { option ->
                FilterChip(
                    selected = selectedFilter == option,
                    onClick = { selectedFilter = option },
                    label = { Text(option) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CoffeeBrown,
                        selectedLabelColor = White,
                        labelColor = CoffeeBrown
                    )
                )
            }
        }

        if (filteredApps.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (selectedFilter == "All") "No one has applied yet."
                    else "No $selectedFilter applications.",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredApps, key = { it.applicationId }) { app ->
                    AdminAppCard(app, viewModel)
                }
            }
        }
    }
}

@Composable
fun AdminAppCard(app: ApplicationModel, viewModel: JobViewModel) {
    var currentStatus by remember(app.status) { mutableStateOf(app.status) }
    val isPending = currentStatus == "Pending"
    var showFullCv by remember { mutableStateOf(false) }

    // --- FULL CV PREVIEW DIALOG ---
    if (showFullCv) {
        AlertDialog(
            onDismissRequest = { showFullCv = false },
            title = { Text("CV / Motivation", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = app.cvDescription, fontSize = 16.sp, color = Color.DarkGray)
                }
            },
            confirmButton = {
                TextButton(onClick = { showFullCv = false }) {
                    Text("Close", color = CoffeeBrown, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = White
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(app.jobTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CoffeeBrown)
                    Text("From: ${app.userEmail}", fontSize = 14.sp, color = Color.Gray)
                }

                Text(
                    text = currentStatus,
                    fontWeight = FontWeight.Bold,
                    color = when(currentStatus) {
                        "Accepted" -> Color(0xFF4CAF50)
                        "Declined" -> Color(0xFFF44336)
                        else -> Color(0xFFFF9800)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- CLICKABLE CV SECTION ---
            Surface(
                onClick = { showFullCv = true },
                color = SoftCream.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Motivation (Click to expand):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CoffeeBrown)
                    Text(
                        text = app.cvDescription,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (app.applicationId.isNotEmpty()) {
                            currentStatus = "Accepted"
                            viewModel.updateApplicationStatus(app.applicationId, "Accepted")
                        }
                    },
                    enabled = isPending,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        disabledContainerColor = if (currentStatus == "Accepted") Color(0xFF4CAF50).copy(0.4f) else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) { Text(if (currentStatus == "Accepted") "Accepted" else "Accept") }

                Button(
                    onClick = {
                        if (app.applicationId.isNotEmpty()) {
                            currentStatus = "Declined"
                            viewModel.updateApplicationStatus(app.applicationId, "Declined")
                        }
                    },
                    enabled = isPending,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336),
                        disabledContainerColor = if (currentStatus == "Declined") Color(0xFFF44336).copy(0.4f) else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) { Text(if (currentStatus == "Declined") "Declined" else "Decline") }
            }
        }
    }
}

@Composable
fun JobItemCard(job: JobModel, onDelete: (String) -> Unit, onEdit: (JobModel) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(job.title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = CoffeeBrown)
                    Text(job.company, color = Color.Gray, fontSize = 15.sp)
                }
                Box {
                    IconButton(onClick = { expanded = true }) { Icon(Icons.Default.MoreVert, null) }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("Edit") }, onClick = { expanded = false; onEdit(job) })
                        DropdownMenuItem(text = { Text("Delete", color = Color.Red) }, onClick = { expanded = false; onDelete(job.jobId) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            AdminJobDetailRow("Location", job.location)
            AdminJobDetailRow("Salary", job.salary)
        }
    }
}

@Composable
fun AdminJobDetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("$label: ", fontWeight = FontWeight.Bold, color = CoffeeBrown, fontSize = 14.sp)
        Text(value, color = Color.DarkGray, fontSize = 14.sp)
    }
}