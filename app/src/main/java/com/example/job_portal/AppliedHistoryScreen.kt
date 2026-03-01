package com.example.job_portal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun AppliedHistoryScreen(viewModel: JobViewModel) {
    val applications = viewModel.userApplications

    LaunchedEffect(Unit) {
        viewModel.fetchUserApplications()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray) // Updated from SoftCream
            .padding(16.dp)
    ) {
        Text(
            text = "My Applications",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryIndigo, // Updated from CoffeeBrown
            modifier = Modifier.padding(bottom = 20.dp, top = 8.dp)
        )

        if (applications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No application history found.",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(applications) { app ->
                    ApplicationStatusCard(app)
                }
            }
        }
    }
}

@Composable
fun ApplicationStatusCard(app: ApplicationModel) {
    // Professional status colors
    val statusColor = when (app.status) {
        "Accepted" -> Color(0xFF2E7D32) // Emerald Green
        "Declined" -> Color(0xFFC62828) // Crimson Red
        else -> Color(0xFFF9A825)       // Amber Orange (Pending)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp) // Slightly higher elevation for modern look
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.jobTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PrimaryIndigo // Updated from CoffeeBrown
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Applied for PathVista Portal",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Status Badge with refined border and soft background
            Surface(
                color = statusColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
            ) {
                Text(
                    text = app.status,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    color = statusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}