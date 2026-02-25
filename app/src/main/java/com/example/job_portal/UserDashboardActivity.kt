package com.example.job_portal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel

class UserDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserDashboardScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen() {
    // Shared ViewModel Instance initialized with the Repository
    val repo = remember { JobRepoImpl() }
    val jobViewModel: JobViewModel = remember { JobViewModel(repo) }

    // State for navigation (Index 4 is used for the Saved Jobs screen)
    var selectedIndex by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Search", Icons.Default.Search),
        NavItem("Settings", Icons.Default.Settings),
        NavItem("Profile", Icons.Default.Person)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when(selectedIndex) {
                            4 -> "Saved Jobs"
                            else -> "Job Portal"
                        },
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
                // We only show the first 4 items in the BottomNav
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                // FIXED: Passing jobViewModel to HomeScreen and SearchScreen
                0 -> HomeScreen(jobViewModel)
                1 -> SearchScreen(jobViewModel)
                2 -> SettingScreen(
                    onSavedJobsClick = { selectedIndex = 4 }
                )
                3 -> ProfileScreen()
                4 -> SavedJobsScreen(jobViewModel)
                else -> HomeScreen(jobViewModel)
            }
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector)