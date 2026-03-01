package com.example.job_portal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background // CRITICAL: This was missing
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.repository.UserRepoImpl
// UI Theme Imports
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel
import com.example.job_portal.viewmodel.UserViewModel

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
    // 1. Initialize Repositories and ViewModels
    val jobRepo = remember { JobRepoImpl() }
    val jobViewModel: JobViewModel = remember { JobViewModel(jobRepo) }

    val userRepo = remember { UserRepoImpl() }
    val userViewModel: UserViewModel = remember { UserViewModel(userRepo) }

    // Navigation State
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Fetch initial data for the user
    LaunchedEffect(Unit) {
        jobViewModel.fetchUserApplications()
    }

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Search", Icons.Default.Search),
        NavItem("Settings", Icons.Default.Settings),
        NavItem("Profile", Icons.Default.Person)
    )

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when(selectedIndex) {
                            3 -> "My Profile"
                            4 -> "Saved Jobs"
                            5 -> "Applied History"
                            else -> "PathVista Portal"
                        },
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = PrimaryIndigo
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = White,
                    scrolledContainerColor = White
                ),
                modifier = Modifier.padding(bottom = 2.dp)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = White,
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = selectedIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedIndex = index },
                        label = {
                            Text(
                                text = item.label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryIndigo,
                            selectedTextColor = PrimaryIndigo,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PrimaryIndigo.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        // The Box container holds the different screens based on navigation
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(BackgroundGray) // This is where the error occurred
        ) {
            when (selectedIndex) {
                0 -> HomeScreen(jobViewModel, userViewModel)
                1 -> SearchScreen(jobViewModel, userViewModel)
                2 -> SettingScreen(
                    viewModel = jobViewModel,
                    onSavedJobsClick = { selectedIndex = 4 },
                    onHistoryClick = { selectedIndex = 5 }
                )
                3 -> ProfileScreen(userViewModel)
                4 -> SavedJobsScreen(jobViewModel, userViewModel)
                5 -> AppliedHistoryScreen(jobViewModel)
                else -> HomeScreen(jobViewModel, userViewModel)
            }
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector)