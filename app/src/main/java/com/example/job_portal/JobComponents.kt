package com.example.job_portal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.ui.theme.CoffeeBrown

// Define it ONCE here. No 'private' needed.
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