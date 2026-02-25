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
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingScreen(
    viewModel: JobViewModel,
    onSavedJobsClick: () -> Unit,
    onHistoryClick: () -> Unit // NEW Callback
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(16.dp)
    ) {
        // --- PROFILE HEADER ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(60.dp).clip(CircleShape).background(CoffeeBrown),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user?.email?.take(1)?.uppercase() ?: "U",
                        color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = "Active User", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CoffeeBrown)
                    Text(text = user?.email ?: "No Email Found", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Account Settings", fontWeight = FontWeight.Bold, color = CoffeeBrown, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))

        SettingsItem(iconRes = R.drawable.baseline_home_24, title = "Edit Profile")

        SettingsItem(
            iconRes = R.drawable.baseline_view_module_24,
            title = "Saved Jobs",
            onClick = onSavedJobsClick
        )

        // REDIRECTS TO APPLIED HISTORY
        SettingsItem(
            iconRes = R.drawable.baseline_search_24,
            title = "Applied History",
            onClick = onHistoryClick
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- FIXED LOGOUT BUTTON ---
        Button(
            onClick = {
                viewModel.clearAllData() // Wipes user-specific state
                FirebaseAuth.getInstance().signOut()
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as Activity).finish()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Logout", color = White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingsItem(iconRes: Int, title: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        color = White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = CoffeeBrown, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 16.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(id = R.drawable.baseline_more_horiz_24), contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        }
    }
}