package com.example.job_portal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepo

class JobViewModel(val repo: JobRepo) : ViewModel() {

    private val _allJobs = MutableLiveData<List<JobModel>>()
    val allJobs: LiveData<List<JobModel>> get() = _allJobs

    // Existing Fetch Function
    fun fetchAllJobs() {
        repo.getAllJobs { success, message, data ->
            if (success) {
                _allJobs.value = data ?: emptyList()
            }
        }
    }

    // Existing Add Function
    fun addJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        repo.addJob(model, callback)
    }

    // 1. ADD THIS: Connects UI to Repo for Deleting
    fun deleteJob(jobId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteJob(jobId) { success, message ->
            if (success) {
                fetchAllJobs() // Refresh the list automatically after deleting
            }
            callback(success, message)
        }
    }

    // 2. ADD THIS: Connects UI to Repo for Updating
    fun updateJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        repo.updateJob(model) { success, message ->
            if (success) {
                fetchAllJobs() // Refresh the list automatically after updating
            }
            callback(success, message)
        }
    }
}