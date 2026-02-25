package com.example.job_portal

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.JobModel
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(jobViewModel: JobViewModel) {
    val jobsFromDb by jobViewModel.allJobs.observeAsState(initial = emptyList())
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

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
                    JobItemCard(job, jobViewModel, userId)
                }
            }
        }
    }
}

@Composable
fun JobItemCard(job: JobModel, viewModel: JobViewModel, userId: String) {
    val isSaved = viewModel.isJobSaved(job)
    val context = LocalContext.current

    // States for the Application Dialog
    var showDialog by remember { mutableStateOf(false) }
    var userEmail by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.email ?: "") }
    var cvDescription by remember { mutableStateOf("") }

    // --- APPLICATION DIALOG ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Apply for ${job.title}", fontWeight = FontWeight.Bold, color = CoffeeBrown) },
            text = {
                Column {
                    OutlinedTextField(
                        value = userEmail,
                        onValueChange = { userEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = cvDescription,
                        onValueChange = { cvDescription = it },
                        label = { Text("Why are you a good fit? (CV Summary)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (userEmail.isNotEmpty() && cvDescription.isNotEmpty()) {
                            // viewModel.submitApplication(job, userEmail, cvDescription) // Add this to VM later
                            Toast.makeText(context, "Application Sent!", Toast.LENGTH_SHORT).show()
                            showDialog = false
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = job.title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = CoffeeBrown)
                    Text(text = job.company, color = Color.Gray, fontSize = 15.sp)
                }

                IconButton(
                    onClick = {
                        if (userId.isNotEmpty()) {
                            viewModel.toggleSaveJob(userId, job)
                        } else {
                            Toast.makeText(context, "Please log in to save jobs", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isSaved) R.drawable.bookmarkblack else R.drawable.save
                        ),
                        contentDescription = "Save Job",
                        modifier = Modifier.size(20.dp),
                        tint = if (isSaved) CoffeeBrown else Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(12.dp))

            HomeJobDetailRow(label = "Location", value = job.location)
            HomeJobDetailRow(label = "Salary", value = job.salary)
            HomeJobDetailRow(label = "Job Type", value = job.type)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDialog = true },
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
fun HomeJobDetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold, color = CoffeeBrown, fontSize = 14.sp)
        Text(text = value, color = Color.DarkGray, fontSize = 14.sp)
    }
}