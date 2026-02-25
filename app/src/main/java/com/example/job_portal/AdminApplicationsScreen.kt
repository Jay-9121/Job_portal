package com.example.job_portal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.ApplicationModel
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel

@Composable
fun AdminReviewScreen(viewModel: JobViewModel) {
    // 1. Reverse the list so newest applicants are at the top
    val allApps = viewModel.allApplications.reversed()

    LaunchedEffect(Unit) {
        viewModel.fetchAllApplications()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(16.dp)
    ) {
        Text(
            text = "Review Applications",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (allApps.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No applications received yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Using 'key' ensures Compose identifies each card correctly
                items(allApps, key = { it.applicationId }) { app ->
                    AdminReviewCard(app, viewModel)
                }
            }
        }
    }
}

@Composable
fun AdminReviewCard(app: ApplicationModel, viewModel: JobViewModel) {
    // We create a local state that tracks the status.
    // This makes the UI change INSTANTLY even if the internet is slow.
    var currentStatus by remember(app.status) { mutableStateOf(app.status) }

    // Buttons are ONLY enabled if the status is exactly "Pending"
    val isPending = currentStatus == "Pending"

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
                    Text("Applicant: ${app.userEmail}", fontSize = 14.sp, color = Color.Gray)
                    // DEBUG TEXT: Remove this after it works
                    Text("ID: ${app.applicationId}", fontSize = 10.sp, color = Color.LightGray)
                }

                val badgeColor = when(currentStatus) {
                    "Accepted" -> Color(0xFF4CAF50)
                    "Declined" -> Color(0xFFF44336)
                    else -> Color(0xFFFF9800)
                }
                Text(currentStatus, color = badgeColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(app.cvDescription, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (app.applicationId.isNotEmpty()) {
                            currentStatus = "Accepted" // Force the UI to lock immediately
                            viewModel.updateApplicationStatus(app.applicationId, "Accepted")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = isPending, // This will become false instantly
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        disabledContainerColor = if (currentStatus == "Accepted") Color(0xFF4CAF50).copy(0.4f) else Color.LightGray
                    )
                ) { Text(if (currentStatus == "Accepted") "Accepted" else "Accept") }

                Button(
                    onClick = {
                        if (app.applicationId.isNotEmpty()) {
                            currentStatus = "Declined" // Force the UI to lock immediately
                            viewModel.updateApplicationStatus(app.applicationId, "Declined")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = isPending, // This will become false instantly
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336),
                        disabledContainerColor = if (currentStatus == "Declined") Color(0xFFF44336).copy(0.4f) else Color.LightGray
                    )
                ) { Text(if (currentStatus == "Declined") "Declined" else "Decline") }
            }
        }
    }
}