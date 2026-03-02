package com.example.job_portal

import android.os.Bundle // FIXED: Added missing import
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
import androidx.compose.ui.platform.testTag // Required for identification in Large Tests
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
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
    var requirements by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            Surface(shadowElevation = 4.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryIndigo)
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = "Post New Job",
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundGray)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
        ) {
            item {
                // Input fields with testTags for instrumented testing
                CustomTextField(value = title, label = "Job Title", tag = "title_input") { title = it }
                CustomTextField(value = company, label = "Company Name", tag = "company_input") { company = it }
                CustomTextField(value = location, label = "Location", tag = "location_input") { location = it }
                CustomTextField(value = salary, label = "Salary Range", tag = "salary_input") { salary = it }
                CustomTextField(value = type, label = "Job Type", tag = "type_input") { type = it }
                CustomTextField(
                    value = requirements,
                    label = "Requirements",
                    isSingleLine = false,
                    tag = "requirements_input"
                ) { requirements = it }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (title.isEmpty() || company.isEmpty() || requirements.isEmpty()) {
                            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val model = JobModel(
                            jobId = "",
                            title = title,
                            company = company,
                            location = location,
                            salary = salary,
                            type = type,
                            requirements = requirements
                        )

                        jobViewModel.addJob(model) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            if (success) {
                                (context as ComponentActivity).finish()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("post_job_button"), // Tag used for Large/End-to-End tests
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Text("Post Job", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    label: String,
    tag: String,
    isSingleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag), // Assigning the testing tag
        shape = RoundedCornerShape(12.dp),
        singleLine = isSingleLine,
        minLines = if (isSingleLine) 1 else 4,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryIndigo,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = PrimaryIndigo,
            unfocusedContainerColor = White,
            focusedContainerColor = White
        )
    )
}