package com.example.job_portal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepoImpl
import com.example.job_portal.ui.theme.CoffeeBrown
import com.example.job_portal.ui.theme.SoftCream
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

    Column(modifier = Modifier.fillMaxSize().background(SoftCream).padding(16.dp)) {
        Text("Edit Job Posting", style = MaterialTheme.typography.headlineMedium, color = CoffeeBrown)
        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(value = title, label = "Job Title") { title = it }
        CustomTextField(value = company, label = "Company") { company = it }
        CustomTextField(value = location, label = "Location") { location = it }
        CustomTextField(value = salary, label = "Salary") { salary = it }
        CustomTextField(value = type, label = "Job Type") { type = it }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                val updatedJob = JobModel(id, title, company, location, salary, type)
                jobViewModel.updateJob(updatedJob) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) (context as ComponentActivity).finish()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
        ) {
            Text("Update Job")
        }
    }
}