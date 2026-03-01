package com.example.job_portal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.UserModel
// UI Theme Imports - Updated to the modern palette
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.UserViewModel

@Composable
fun ProfileScreen(userViewModel: UserViewModel) {
    val currentUser = userViewModel.getCurrentUser()
    val userId = currentUser?.uid ?: ""
    val context = LocalContext.current

    // Observe the user data from ViewModel
    val userData by userViewModel.users.observeAsState()

    // Fetch user details when the screen opens
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            userViewModel.getUserById(userId)
        }
    }

    // Local states for editing
    var firstName by remember(userData) { mutableStateOf(userData?.firstName ?: "") }
    var lastName by remember(userData) { mutableStateOf(userData?.lastName ?: "") }
    var phone by remember(userData) { mutableStateOf(userData?.phoneNumber ?: "") }
    var location by remember(userData) { mutableStateOf(userData?.location ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray) // Updated from SoftCream
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "My Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryIndigo // Updated from CoffeeBrown
        )

        Text(
            text = "Email: ${currentUser?.email ?: "Not logged in"}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Profile Input Fields
        ProfileTextField(label = "First Name", value = firstName, onValueChange = { firstName = it })
        ProfileTextField(label = "Last Name", value = lastName, onValueChange = { lastName = it })
        ProfileTextField(label = "Phone Number", value = phone, onValueChange = { phone = it })
        ProfileTextField(label = "Location", value = location, onValueChange = { location = it })

        Spacer(modifier = Modifier.height(40.dp))

        // Update Button
        Button(
            onClick = {
                if (userId.isNotEmpty()) {
                    val updatedUser = UserModel(
                        userId = userId,
                        email = currentUser?.email ?: "",
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phone,
                        location = location,
                        dob = userData?.dob ?: "",
                        gender = userData?.gender ?: ""
                    )
                    userViewModel.updateProfile(userId, updatedUser) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo), // Updated
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Update Profile",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = PrimaryIndigo, // Updated from CoffeeBrown
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedBorderColor = PrimaryIndigo,
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                cursorColor = PrimaryIndigo,
                focusedLabelColor = PrimaryIndigo
            )
        )
    }
}