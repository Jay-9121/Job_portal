package com.example.job_portal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel

class AddJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddJobBody()
        }
    }
}

@Composable
fun AddJobBody() {
    val jobViewModel = remember { JobViewModel(JobRepoImpl()) }

    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    // --- NEW FIELD ---
    var requirements by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth().background(CoffeeBrown).padding(16.dp)) {
                Text("Post New Job", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SoftCream)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                CustomTextField(value = title, label = "Job Title") { title = it }
                CustomTextField(value = company, label = "Company Name") { company = it }
                CustomTextField(value = location, label = "Location (City or Remote)") { location = it }
                CustomTextField(value = salary, label = "Salary (e.g. $50k - $70k)") { salary = it }
                CustomTextField(value = type, label = "Job Type (Full-time/Part-time)") { type = it }

                // --- ADDED REQUIREMENTS TEXT FIELD ---
                CustomTextField(
                    value = requirements,
                    label = "Job Requirements (Skills, Experience, etc.)",
                    isSingleLine = false // Allow multiple lines for requirements
                ) { requirements = it }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (title.isEmpty() || company.isEmpty() || requirements.isEmpty()) {
                            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // --- UPDATED MODEL CALL ---
                        val model = JobModel(
                            jobId = "",
                            title = title,
                            company = company,
                            location = location,
                            salary = salary,
                            type = type,
                            requirements = requirements // Passing the requirements
                        )

                        jobViewModel.addJob(model) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            if (success) {
                                (context as ComponentActivity).finish()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
                ) {
                    Text("Post Job", color = White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    label: String,
    isSingleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = isSingleLine,
        minLines = if (isSingleLine) 1 else 3,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CoffeeBrown,
            unfocusedContainerColor = White,
            focusedContainerColor = White
        )
    )
}