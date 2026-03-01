package com.example.job_portal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.job_portal.repository.UserRepoImpl
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.SecondaryBlue
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}

@Composable
fun LoginBody() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val snackbarHostState = remember { SnackbarHostState() }
    val repo = remember { UserRepoImpl() }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Text("Ok", color = PrimaryIndigo, modifier = Modifier.clickable { showDialog = false }.padding(8.dp))
                    },
                    dismissButton = {
                        Text("Cancel", color = Color.Gray, modifier = Modifier.clickable { showDialog = false }.padding(8.dp))
                    },
                    title = { Text("Confirm", color = PrimaryIndigo, fontWeight = FontWeight.Bold) },
                    text = { Text("Are you sure you want to proceed?") },
                    properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
                    containerColor = White,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // Pushes content down slightly for a balanced look
            Spacer(modifier = Modifier.height(80.dp))

            // Brand/App Icon could go here
            Icon(
                painter = painterResource(id = R.drawable.baseline_work_24), // Example icon
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = PrimaryIndigo
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Sign In",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    color = PrimaryIndigo,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Access your PathVista account to manage your applications and profile.",
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 40.dp),
                style = TextStyle(textAlign = TextAlign.Center, color = Color.Gray, fontSize = 15.sp)
            )

            // --- Credential Fields ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                label = { Text("Email Address") },
                placeholder = { Text("example@mail.com") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = PrimaryIndigo,
                    unfocusedContainerColor = White,
                    focusedContainerColor = White
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
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
                visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = PrimaryIndigo,
                    unfocusedContainerColor = White,
                    focusedContainerColor = White
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                "Forgot Password?",
                color = SecondaryBlue,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 12.dp)
                    .clickable {
                        context.startActivity(Intent(context, ForgetPasswordActivity::class.java))
                    },
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- Action Button ---
            Button(
                onClick = {
                    val trimmedEmail = email.trim()
                    if (trimmedEmail.isEmpty() || password.isEmpty()) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Please enter all credentials") }
                        return@Button
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Invalid email format") }
                        return@Button
                    }

                    repo.login(trimmedEmail, password) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                            val intent = if (trimmedEmail == "admin@gmail.com") {
                                Intent(context, DashboardActivity::class.java)
                            } else {
                                Intent(context, UserDashboardActivity::class.java)
                            }
                            intent.putExtra("email", trimmedEmail)
                            context.startActivity(intent)
                            activity.finish()
                        } else {
                            coroutineScope.launch { snackbarHostState.showSnackbar(message ?: "Authentication failed") }
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
            ) {
                Text("Log In", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                buildAnnotatedString {
                    append("New to PathVista? ")
                    withStyle(SpanStyle(color = PrimaryIndigo, fontWeight = FontWeight.ExtraBold)) {
                        append("Create Account")
                    }
                },
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, RegistrationActivity::class.java))
                    activity.finish()
                },
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}