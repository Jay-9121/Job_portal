package com.example.job_portal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.UserModel
import com.example.job_portal.repository.UserRepoImpl
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.UserViewModel
// CRITICAL IMPORTS FOR COROUTINES
import kotlinx.coroutines.launch

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrationBody()
        }
    }
}

@Composable
fun RegistrationBody() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as Activity

    // State for Coroutines and Snackbars
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val repo = remember { UserRepoImpl() }
    val viewModel = remember { UserViewModel(repo) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = BackgroundGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundGray)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Create Account",
                style = TextStyle(
                    fontSize = 32.sp,
                    color = PrimaryIndigo,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                "Join PathVista Portal today and discover your next big career move.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 32.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomOutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "First Name",
                    modifier = Modifier.weight(1f)
                )
                CustomOutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Last Name",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            var visibility by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Min. 6 characters") },
                visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.baseline_visibility_off_24)
                            else
                                painterResource(R.drawable.baseline_visibility_24),
                            contentDescription = null,
                            tint = PrimaryIndigo
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = PrimaryIndigo,
                    unfocusedContainerColor = White,
                    focusedContainerColor = White
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    // VALIDATION LOGIC
                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.length < 6) {
                        // FIX: Launched inside coroutineScope
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please fill all fields. Password must be 6+ chars.",
                                duration = SnackbarDuration.Short
                            )
                        }
                        return@Button
                    }

                    // FIREBASE REGISTRATION
                    viewModel.register(email, password) { success, message, userId ->
                        if (success) {
                            val newUser = UserModel(
                                userId = userId,
                                email = email,
                                firstName = firstName,
                                lastName = lastName
                            )

                            viewModel.addUserToDatabase(userId, newUser) { dbSuccess, dbMessage ->
                                if (dbSuccess) {
                                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_LONG).show()
                                    context.startActivity(Intent(context, LoginActivity::class.java))
                                    activity.finish()
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Database error: $dbMessage")
                                    }
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message ?: "Registration failed")
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
            ) {
                Text("Sign Up", fontSize = 16.sp, color = White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(color = PrimaryIndigo, fontWeight = FontWeight.Bold)) {
                        append("Log In")
                    }
                },
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    activity.finish()
                },
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        label = { Text(label) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryIndigo,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = PrimaryIndigo,
            unfocusedContainerColor = White,
            focusedContainerColor = White
        ),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    )
}