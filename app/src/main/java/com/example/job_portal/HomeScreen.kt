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
import com.example.job_portal.model.UserModel
// UI Theme Imports - Updated to the modern Indigo palette
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.SecondaryBlue
import com.example.job_portal.ui.theme.AccentAmber
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.example.job_portal.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(jobViewModel: JobViewModel, userViewModel: UserViewModel) {
    val jobsFromDb by jobViewModel.allJobs.observeAsState(initial = emptyList())
    val userData by userViewModel.users.observeAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

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
            .background(BackgroundGray) // Updated background
            .padding(16.dp)
    ) {
        Text(
            text = "Popular Opportunities",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryIndigo, // Updated text color
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (jobsFromDb.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryIndigo)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(jobsFromDb) { job ->
                    JobItemCard(job, jobViewModel, userId, userData)
                }
            }
        }
    }
}

@Composable
fun JobItemCard(
    job: JobModel,
    viewModel: JobViewModel,
    userId: String,
    userData: UserModel?
) {
    val isSaved = viewModel.isJobSaved(job)
    val context = LocalContext.current
    val hasApplied = remember(viewModel.userApplications, job.jobId) {
        viewModel.userApplications.any { it.jobId == job.jobId }
    }

    var showDialog by remember { mutableStateOf(false) }

    var userEmail by remember(userData) { mutableStateOf(userData?.email ?: "") }
    var cvDescription by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Apply for ${job.title}", fontWeight = FontWeight.Bold, color = PrimaryIndigo) },
            text = {
                Column {
                    Text(
                        text = "Applying as: ${userData?.firstName ?: ""} ${userData?.lastName ?: ""}".trim().ifEmpty { userEmail },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = userEmail,
                        onValueChange = { userEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            focusedLabelColor = PrimaryIndigo
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cvDescription,
                        onValueChange = { cvDescription = it },
                        label = { Text("CV Summary / Why you?") },
                        placeholder = { Text("Tell the recruiter about your experience...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            focusedLabelColor = PrimaryIndigo
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (userEmail.isNotEmpty() && cvDescription.isNotEmpty()) {
                            viewModel.submitJobApplication(job, userEmail, cvDescription)
                            Toast.makeText(context, "Application Sent!", Toast.LENGTH_SHORT).show()
                            showDialog = false
                            cvDescription = ""
                        } else {
                            Toast.makeText(context, "Please complete the form", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Text("Submit Application", color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = White,
            shape = RoundedCornerShape(16.dp)
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
                    Text(text = job.title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = PrimaryIndigo)
                    Text(text = job.company, color = SecondaryBlue, fontSize = 15.sp)
                }

                IconButton(
                    onClick = {
                        if (userId.isNotEmpty()) viewModel.toggleSaveJob(userId, job)
                        else Toast.makeText(context, "Please log in", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isSaved) R.drawable.bookmarkblack else R.drawable.save
                        ),
                        contentDescription = "Save Job",
                        modifier = Modifier.size(20.dp),
                        tint = if (isSaved) AccentAmber else Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(12.dp))

            HomeJobDetailRow(label = "Location", value = job.location)
            HomeJobDetailRow(label = "Salary", value = job.salary)
            HomeJobDetailRow(label = "Job Type", value = job.type)

            if (job.requirements.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Requirements:",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryIndigo,
                    fontSize = 14.sp
                )
                Text(
                    text = job.requirements,
                    color = Color.DarkGray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { if (!hasApplied) showDialog = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !hasApplied,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryIndigo,
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (hasApplied) "Applied" else "Apply Now",
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HomeJobDetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold, color = PrimaryIndigo, fontSize = 14.sp)
        Text(text = value, color = Color.DarkGray, fontSize = 14.sp)
    }
}