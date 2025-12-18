package com.example.job_portal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.window.DialogProperties
import com.example.job_portal.DashboardActivity
import com.example.job_portal.RegistrationActivity
import com.example.job_portal.repository.UserRepoImpl
import com.example.job_portal.ui.theme.Black
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.CoffeeCream
import com.example.job_portal.ui.theme.CoffeeLight
import com.example.job_portal.ui.theme.SoftCream
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
    val repo = UserRepoImpl()
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SoftCream) // Updated to themed background
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Text("Ok", color = CoffeeBrown, modifier = Modifier.clickable { showDialog = false })
                    },
                    dismissButton = {
                        Text("Cancel", color = CoffeeBrown.copy(0.6f), modifier = Modifier.clickable { showDialog = false })
                    },
                    title = { Text("Confirm", color = CoffeeBrown) },
                    text = { Text("Are you sure you want to delete?") },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                "Sign In",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    color = CoffeeBrown, // Updated to CoffeeBrown
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Welcome to PathVista Portal. Find your next career move with ease.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Black.copy(0.6f),
                    fontSize = 15.sp
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                SocialMediaCard(
                    Modifier
                        .height(60.dp)
                        .weight(1f),
                    com.example.job_portal.R.drawable.face,
                    "Facebook"
                )
                Spacer(modifier = Modifier.width(15.dp))

                SocialMediaCard(
                    Modifier
                        .height(60.dp)
                        .weight(1f),
                    com.example.job_portal.R.drawable.gmail,
                    "Gmail"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = CoffeeBrown.copy(alpha = 0.2f)
                )
                Text("OR", modifier = Modifier.padding(horizontal = 20.dp), color = CoffeeBrown.copy(alpha = 0.5f))
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = CoffeeBrown.copy(alpha = 0.2f)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text("abc@gmail.com") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = CoffeeCream, // Updated to CoffeeCream
                    focusedContainerColor = CoffeeCream,
                    focusedIndicatorColor = CoffeeBrown,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("********") },
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(com.example.job_portal.R.drawable.baseline_visibility_off_24)
                            else
                                painterResource(com.example.job_portal.R.drawable.baseline_visibility_24),
                            contentDescription = null,
                            tint = CoffeeBrown
                        )
                    }
                },
                visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = CoffeeCream, // Updated to CoffeeCream
                    focusedContainerColor = CoffeeCream,
                    focusedIndicatorColor = CoffeeBrown,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp)
            )

            Text(
                "Forget Password?",
                color = CoffeeLight, // Updated to CoffeeLight
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable {
                        val intent = Intent(context, ForgetPasswordActivity::class.java)
                        context.startActivity(intent)
                    },
                style = TextStyle(textAlign = TextAlign.End, fontWeight = FontWeight.Medium)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Email and password cannot be empty")
                        }
                        return@Button
                    }

                    repo.login(email, password) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, DashboardActivity::class.java)
                            intent.putExtra("email", email)
                            context.startActivity(intent)
                            activity.finish()
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message ?: "Login failed")
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown) // Updated to CoffeeBrown
            ) {
                Text("Log In", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                buildAnnotatedString {
                    append("Don't have account? ")
                    withStyle(SpanStyle(color = CoffeeBrown, fontWeight = FontWeight.Bold)) {
                        append("Sign up")
                    }
                },
                modifier = Modifier.clickable {
                    val intent = Intent(context, RegistrationActivity::class.java)
                    context.startActivity(intent)
                    activity.finish()
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SocialMediaCard(modifier: Modifier, image: Int, label: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(label, color = CoffeeBrown, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginBody()
}