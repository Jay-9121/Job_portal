package com.example.job_portal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
// UI Theme Imports - Updated to the modern palette
import com.example.job_portal.ui.theme.PrimaryIndigo
import com.example.job_portal.ui.theme.BackgroundGray
import com.example.job_portal.ui.theme.White
import com.example.job_portal.viewmodel.JobViewModel

class EditJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve data passed from DashboardActivity
        val jobId = intent.getStringExtra("jobId") ?: ""
        val oldTitle = intent.getStringExtra("title") ?: ""
        val oldCompany = intent.getStringExtra("company") ?: ""
        val oldLocation = intent.getStringExtra("location") ?: ""
        val oldSalary = intent.getStringExtra("salary") ?: ""
        val oldType = intent.getStringExtra("type") ?: ""

        setContent {
            EditJobBody(jobId, oldTitle, oldCompany, oldLocation, oldSalary, oldType)
        }
    }
}

@Composable
fun EditJobBody(id: String, t: String, c: String, l: String, s: String, ty: String) {
    val jobViewModel = remember { JobViewModel(JobRepoImpl()) }
    var title by remember { mutableStateOf(t) }
    var company by remember { mutableStateOf(c) }
    var location by remember { mutableStateOf(l) }
    var salary by remember { mutableStateOf(s) }
    var type by remember { mutableStateOf(ty) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray) // Updated background
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Edit Job Posting",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryIndigo // Updated title color
        )

        Text(
            text = "Update the details for this position.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Updated TextFields with Indigo styling
        EditJobTextField(value = title, label = "Job Title") { title = it }
        Spacer(modifier = Modifier.height(16.dp))

        EditJobTextField(value = company, label = "Company Name") { company = it }
        Spacer(modifier = Modifier.height(16.dp))

        EditJobTextField(value = location, label = "Location") { location = it }
        Spacer(modifier = Modifier.height(16.dp))

        EditJobTextField(value = salary, label = "Salary Range") { salary = it }
        Spacer(modifier = Modifier.height(16.dp))

        EditJobTextField(value = type, label = "Job Type (e.g. Full-time)") { type = it }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                val updatedJob = JobModel(id, title, company, location, salary, type)
                jobViewModel.updateJob(updatedJob) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) (context as ComponentActivity).finish()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo) // Updated button color
        ) {
            Text("Update Job", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = White)
        }
    }
}

@Composable
fun EditJobTextField(value: String, label: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryIndigo,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = PrimaryIndigo,
            unfocusedContainerColor = White,
            focusedContainerColor = White
        )
    )
}