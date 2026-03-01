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
// Updated UI Theme Imports
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel

@Composable
fun AdminReviewScreen(viewModel: JobViewModel) {
    val allApps = viewModel.allApplications.reversed()

    LaunchedEffect(Unit) {
        viewModel.fetchAllApplications()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray) // Updated from SoftCream
            .padding(16.dp)
    ) {
        Text(
            text = "Review Applications",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryIndigo // Updated from CoffeeBrown
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
                items(allApps, key = { it.applicationId }) { app ->
                    AdminReviewCard(app, viewModel)
                }
            }
        }
    }
}

@Composable
fun AdminReviewCard(app: ApplicationModel, viewModel: JobViewModel) {
    var currentStatus by remember(app.status) { mutableStateOf(app.status) }
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
                    Text(
                        text = app.jobTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryIndigo // Updated
                    )
                    Text(
                        text = "Applicant: ${app.userEmail}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                val badgeColor = when(currentStatus) {
                    "Accepted" -> Color(0xFF2E7D32) // Deeper Emerald
                    "Declined" -> Color(0xFFC62828) // Deeper Crimson
                    else -> Color(0xFFF9A825) // Amber
                }

                Surface(
                    color = badgeColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = currentStatus,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cover Letter / Summary:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Text(
                text = app.cvDescription,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Accept Button
                Button(
                    onClick = {
                        if (app.applicationId.isNotEmpty()) {
                            currentStatus = "Accepted"
                            viewModel.updateApplicationStatus(app.applicationId, "Accepted")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = isPending,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32),
                        disabledContainerColor = if (currentStatus == "Accepted") Color(0xFF2E7D32).copy(0.4f) else Color.LightGray
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (currentStatus == "Accepted") "Accepted" else "Accept",
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Decline Button
                Button(
                    onClick = {
                        if (app.applicationId.isNotEmpty()) {
                            currentStatus = "Declined"
                            viewModel.updateApplicationStatus(app.applicationId, "Declined")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = isPending,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC62828),
                        disabledContainerColor = if (currentStatus == "Declined") Color(0xFFC62828).copy(0.4f) else Color.LightGray
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (currentStatus == "Declined") "Declined" else "Decline",
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}