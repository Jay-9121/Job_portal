package com.example.job_portal.repository

import com.example.job_portal.model.JobModel

interface JobRepo {
    fun addJob(model: JobModel, callback: (Boolean, String) -> Unit)
    fun getAllJobs(callback: (Boolean, String, List<JobModel>?) -> Unit)
    fun updateJob(model: JobModel, callback: (Boolean, String) -> Unit) // Member 1
    fun deleteJob(jobId: String, callback: (Boolean, String) -> Unit)   // Member 2
}