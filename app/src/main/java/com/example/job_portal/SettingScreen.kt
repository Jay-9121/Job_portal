package com.example.job_portal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.job_portal.ui.theme.CoffeeBrown

@Composable
fun SettingScreen() {
    Text("Settings: App Preferences", modifier = Modifier.fillMaxSize().padding(16.dp), color = CoffeeBrown)
}