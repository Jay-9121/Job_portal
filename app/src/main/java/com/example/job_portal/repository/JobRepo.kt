package com.example.job_portal.repository

import com.example.job_portal.model.ApplicationModel
import com.example.job_portal.model.JobModel

interface JobRepo {
    // Basic Job CRUD
    fun addJob(model: JobModel, callback: (Boolean, String) -> Unit)
    fun getAllJobs(callback: (Boolean, String, List<JobModel>?) -> Unit)
    fun updateJob(model: JobModel, callback: (Boolean, String) -> Unit)
    fun deleteJob(jobId: String, callback: (Boolean, String) -> Unit)

    // Saved Jobs Persistence
    fun saveJobToDb(userId: String, jobId: String, callback: (Boolean) -> Unit)
    fun unsaveJobFromDb(userId: String, jobId: String, callback: (Boolean) -> Unit)
    fun getSavedJobIds(userId: String, callback: (List<String>) -> Unit)

    // Application Logic
    fun submitApplication(application: ApplicationModel, callback: (Boolean) -> Unit)
    fun getUserApplications(userEmail: String, callback: (List<ApplicationModel>) -> Unit)
    fun getAllApplications(callback: (List<ApplicationModel>) -> Unit) // For Admin
    fun updateApplicationStatus(applicationId: String, newStatus: String, callback: (Boolean) -> Unit)
}