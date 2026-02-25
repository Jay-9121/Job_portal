package com.example.job_portal.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepo

class JobViewModel(val repo: JobRepo) : ViewModel() {

    // LiveData for multiple jobs (Home Screen)
    private val _allJobs = MutableLiveData<List<JobModel>?>()
    val allJobs: MutableLiveData<List<JobModel>?> get() = _allJobs

    // LiveData for a single job (Details Screen)
    private val _singleJob = MutableLiveData<JobModel?>()
    val singleJob: MutableLiveData<JobModel?> get() = _singleJob

    // 1. Create/Add Job
    fun addJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        repo.addJob(model, callback)
    }

    // 2. Read/Fetch all Jobs (For your Home Screen)
    fun fetchAllJobs() {
        repo.getAllJobs { success, message, data ->
            if (success) {
                _allJobs.postValue(data)
            }
        }
    }

    // 3. Update Job (For Edit Job screen)
    fun updateJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        repo.updateJob(model, callback)
    }

    // 4. Delete Job
    fun deleteJob(jobId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteJob(jobId, callback)
    }

    // 5. Get Job by ID (If you want to view details of one specific job)
    fun getJobById(jobId: String) {
        // You would need fun getJobById in JobRepo for this
        // but for now, you can filter from the existing list:
        val job = _allJobs.value?.find { it.jobId == jobId }
        _singleJob.postValue(job)
    }
}