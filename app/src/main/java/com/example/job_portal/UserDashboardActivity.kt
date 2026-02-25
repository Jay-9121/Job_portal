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
import com.example.job_portal.repository.UserRepoImpl
import com.example.job_portal.ui.theme.CoffeeBrown
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
    // 1. Initialize both Repositories and ViewModels
    val jobRepo = remember { JobRepoImpl() }
    val jobViewModel: JobViewModel = remember { JobViewModel(jobRepo) }

    val userRepo = remember { UserRepoImpl() }
    val userViewModel: UserViewModel = remember { UserViewModel(userRepo) }

    // Navigation State
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Fetch initial data
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
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when(selectedIndex) {
                            3 -> "My Profile"
                            4 -> "Saved Jobs"
                            5 -> "Applied History"
                            else -> "Job Portal"
                        },
                        fontWeight = FontWeight.Bold,
                        color = CoffeeBrown
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = White, contentColor = CoffeeBrown) {
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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selectedIndex) {
                // FIXED: Now passing userViewModel to all screens that require it for JobItemCard
                0 -> HomeScreen(jobViewModel, userViewModel)
                1 -> SearchScreen(jobViewModel, userViewModel)

                2 -> SettingScreen(
                    viewModel = jobViewModel,
                    onSavedJobsClick = { selectedIndex = 4 },
                    onHistoryClick = { selectedIndex = 5 }
                )

                3 -> ProfileScreen(userViewModel)

                // FIXED: SavedJobsScreen also needs userViewModel to render the JobItemCard
                4 -> SavedJobsScreen(jobViewModel, userViewModel)

                5 -> AppliedHistoryScreen(jobViewModel)

                else -> HomeScreen(jobViewModel, userViewModel)
            }
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector)