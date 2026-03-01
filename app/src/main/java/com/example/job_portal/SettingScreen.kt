package com.example.job_portal

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// UI Theme Imports - Updated to the modern palette
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingScreen(
    viewModel: JobViewModel,
    onSavedJobsClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray) // Updated from SoftCream
            .padding(20.dp)
    ) {
        // --- PROFILE HEADER ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Avatar with Indigo Background
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(PrimaryIndigo.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user?.email?.take(1)?.uppercase() ?: "U",
                        color = PrimaryIndigo,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Active Account",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = PrimaryIndigo
                    )
                    Text(
                        text = user?.email ?: "No Email Found",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Account Settings",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = PrimaryIndigo,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        // Navigation Items
        SettingsItem(
            iconRes = R.drawable.baseline_person_24, // Suggested icon change
            title = "Edit Profile"
        )

        SettingsItem(
            iconRes = R.drawable.baseline_view_module_24,
            title = "Saved Jobs",
            onClick = onSavedJobsClick
        )

        SettingsItem(
            iconRes = R.drawable.baseline_history_24, // Suggested icon change
            title = "Applied History",
            onClick = onHistoryClick
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- LOGOUT BUTTON ---
        Button(
            onClick = {
                viewModel.clearAllData()
                FirebaseAuth.getInstance().signOut()
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as Activity).finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            // Professional Red for Logout
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_exit_to_app_24), // Add a logout icon
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun SettingsItem(iconRes: Int, title: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        color = White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = PrimaryIndigo,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.baseline_chevron_right_24), // Use chevron for better UX
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}