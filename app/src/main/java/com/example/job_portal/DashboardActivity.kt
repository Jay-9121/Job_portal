package com.example.job_portal

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.job_portal.R
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.CoffeeCream
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody() {

    val context = LocalContext.current
    val activity = context as Activity

    // Data class for navigation items
    data class NavItem(val label: String, val icon: Int)

    var selectedIndex by remember { mutableStateOf(0) }

    // Navigation list updated for Job Portal: Home, Search, Setting, Profile
    val listNav = listOf(
        NavItem(
            label = "Home",
            icon = R.drawable.baseline_home_24,
        ),
        NavItem(
            label = "Search",
            icon = R.drawable.baseline_search_24,
        ),
        NavItem(
            label = "Setting",
            icon = R.drawable.baseline_settings_24,
        ),
        NavItem(
            label = "Profile",
            icon = R.drawable.baseline_person_24,
        )
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = CoffeeBrown, // Updated to CoffeeBrown
                contentColor = White,
                onClick = {
                    // Logic for adding a new job or post
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Post"
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoffeeBrown, // Updated to CoffeeBrown
                    actionIconContentColor = White,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
                title = { Text("PathVista Portal") },
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Action 1 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_attach_file_24),
                            contentDescription = "Attach"
                        )
                    }
                    IconButton(onClick = { /* Action 2 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_more_vert_24),
                            contentDescription = "More Options"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = White // Keep a clean bottom bar background
            ) {
                listNav.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(item.label)
                        },
                        onClick = {
                            selectedIndex = index
                        },
                        selected = selectedIndex == index,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoffeeBrown,
                            selectedTextColor = CoffeeBrown,
                            indicatorColor = CoffeeCream,
                            unselectedIconColor = CoffeeBrown.copy(alpha = 0.6f),
                            unselectedTextColor = CoffeeBrown.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SoftCream) // Consistent background with Registration
        ) {
            // Routing based on selected index
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
