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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.R
import com.example.job_portal.model.UserModel
import com.example.job_portal.repository.UserRepoImpl
import com.example.job_portal.ui.theme.Black
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.CoffeeCream
import com.example.job_portal.ui.theme.CoffeeLight
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.UserViewModel
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
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val repo = remember { UserRepoImpl() }
    val viewModel = remember { UserViewModel(repo) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SoftCream) // Updated background color
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Create Your Account",
                style = TextStyle(
                    fontSize = 28.sp,
                    color = CoffeeBrown, // Updated to CoffeeBrown
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                "Join our job portal today and find your next opportunity.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Black.copy(0.6f),
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomOutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "First Name",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                CustomOutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Last Name",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            CustomOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email Address",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            var visibility by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password (min 6 characters)") },
                visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.baseline_visibility_off_24)
                            else
                                painterResource(R.drawable.baseline_visibility_24),
                            contentDescription = if (visibility) "Hide password" else "Show password",
                            tint = CoffeeBrown // Icon tint updated
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = CoffeeCream, // Updated color
                    focusedContainerColor = CoffeeCream,
                    focusedIndicatorColor = CoffeeBrown, // Updated color
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.length < 6) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Please fill all fields and use a password of at least 6 characters.",
                                withDismissAction = true
                            )
                        }
                        return@Button
                    }

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
                                    val intent = Intent(context, LoginActivity::class.java)
                                    context.startActivity(intent)
                                    activity.finish()
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Database save failed: $dbMessage")
                                    }
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message ?: "Authentication failed")
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown) // Updated button color
            ) {
                Text("Sign Up", fontSize = 18.sp, color = White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(color = CoffeeBrown, fontWeight = FontWeight.SemiBold)) {
                        append("Log In")
                    }
                }, modifier = Modifier.clickable {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    activity.finish()
                })
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        placeholder = { Text(placeholder) },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = CoffeeCream, // Updated to CoffeeCream
            focusedContainerColor = CoffeeCream,
            focusedIndicatorColor = CoffeeBrown, // Updated to CoffeeBrown
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier.height(55.dp),
        shape = RoundedCornerShape(15.dp)
    )
}

@Preview
@Composable
fun RegistrationPreview() {
    RegistrationBody()
}