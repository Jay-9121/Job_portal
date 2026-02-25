package com.example.job_portal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge // Import this
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.White

class UserDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. This ensures status bar (battery/time) is visible and styled
        enableEdgeToEdge()
        setContent {
            UserDashboardScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen() {
    var selectedIndex by remember { mutableStateOf(0) }

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Search", Icons.Default.Search),
        NavItem("Settings", Icons.Default.Settings),
        NavItem("Profile", Icons.Default.Person)
    )

    Scaffold(
        // 2. Added TopAppBar for the Title
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Job Portal",
                        fontWeight = FontWeight.Bold,
                        color = CoffeeBrown
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = White,
                contentColor = CoffeeBrown
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        label = { Text(item.label) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoffeeBrown,
                            selectedTextColor = CoffeeBrown,
                            unselectedIconColor = androidx.compose.ui.graphics.Color.Gray,
                            indicatorColor = White
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        // 3. paddingValues ensures content starts below TopBar and stays above BottomBar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> SettingScreen()
                3 -> ProfileScreen()
                else -> HomeScreen()
            }
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector)

