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
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
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

    // Local states for editing, initialized with data from DB
    // remember(userData) ensures that when the database data loads, the text fields update
    var firstName by remember(userData) { mutableStateOf(userData?.firstName ?: "") }
    var lastName by remember(userData) { mutableStateOf(userData?.lastName ?: "") }
    var phone by remember(userData) { mutableStateOf(userData?.phoneNumber ?: "") }
    var location by remember(userData) { mutableStateOf(userData?.location ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "My Profile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown
        )
        Text(
            text = "Email: ${currentUser?.email ?: "Not logged in"}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileTextField(label = "First Name", value = firstName, onValueChange = { firstName = it })
        ProfileTextField(label = "Last Name", value = lastName, onValueChange = { lastName = it })
        ProfileTextField(label = "Phone Number", value = phone, onValueChange = { phone = it })
        ProfileTextField(label = "Location", value = location, onValueChange = { location = it })

        Spacer(modifier = Modifier.height(30.dp))

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
                        // Retaining old data for fields not in this form
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
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Update Profile",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProfileTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
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
                focusedBorderColor = CoffeeBrown,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = CoffeeBrown
            )
        )
    }
}