package com.example.job_portal

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White

@Composable
fun SettingScreen(onSavedJobsClick: () -> Unit) { // FIXED: Added the callback parameter
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
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(CoffeeBrown),
                    contentAlignment = Alignment.Center
                ) {
                    Text("JD", color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "John Doe",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CoffeeBrown
                    )
                    Text(
                        text = "john.doe@email.com",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- ACCOUNT SETTINGS ---
        Text(
            text = "Account Settings",
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        SettingsItem(
            iconRes = R.drawable.baseline_home_24,
            title = "Edit Profile",
            onClick = { /* Navigate to Edit Profile later */ }
        )

        // FIXED: Connected the "Saved Jobs" item to the callback
        SettingsItem(
            iconRes = R.drawable.baseline_view_module_24,
            title = "Saved Jobs",
            onClick = onSavedJobsClick
        )

        SettingsItem(
            iconRes = R.drawable.baseline_search_24,
            title = "Applied History",
            onClick = { /* Navigate to history later */ }
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- LOGOUT BUTTON ---
        Button(
            onClick = { /* Handle Logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingsItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit = {} // FIXED: Added onClick parameter to the helper
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // FIXED: Trigger the action on click
        color = White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = CoffeeBrown,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 16.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}