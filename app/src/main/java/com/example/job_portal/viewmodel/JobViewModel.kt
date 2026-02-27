package com.example.job_portal.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.job_portal.model.ApplicationModel
import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepo
import com.google.firebase.auth.FirebaseAuth

class JobViewModel(private val repo: JobRepo) : ViewModel() {

    // --- JOB LIST LOGIC ---
    private val _allJobs = MutableLiveData<List<JobModel>>()
    val allJobs: LiveData<List<JobModel>> get() = _allJobs

    fun fetchAllJobs() {
        repo.getAllJobs { success, _, data ->
            if (success) {
                _allJobs.postValue(data ?: emptyList())
            }
        }
    }

    // --- SAVED JOBS LOGIC (USER SPECIFIC) ---
    private val _savedJobIds = mutableStateListOf<String>()
    val savedJobIds: List<String> = _savedJobIds

    fun fetchSavedJobs(userId: String) {
        repo.getSavedJobIds(userId) { ids ->
            _savedJobIds.clear()
            _savedJobIds.addAll(ids)
        }
    }

    fun toggleSaveJob(userId: String, job: JobModel) {
        if (_savedJobIds.contains(job.jobId)) {
            repo.unsaveJobFromDb(userId, job.jobId) { success ->
                if (success) _savedJobIds.remove(job.jobId)
            }
        } else {
            repo.saveJobToDb(userId, job.jobId) { success ->
                if (success) _savedJobIds.add(job.jobId)
            }
        }
    }

    fun isJobSaved(job: JobModel): Boolean {
        return _savedJobIds.contains(job.jobId)
    }

    // --- APPLICATION LOGIC (USER SIDE) ---
    private val _userApplications = mutableStateListOf<ApplicationModel>()
    val userApplications: List<ApplicationModel> = _userApplications

    fun submitJobApplication(job: JobModel, email: String, cv: String) {
        Log.d("FIREBASE_SUBMIT", "Applying for: ${job.title} by $email")

        if (email.isEmpty()) {
            Log.e("FIREBASE_SUBMIT", "Error: User email is empty.")
            return
        }

        val newApp = ApplicationModel(
            jobId = job.jobId,
            jobTitle = job.title,
            userEmail = email,
            cvDescription = cv,
            status = "Pending"
        )

        repo.submitApplication(newApp) { success ->
            if (success) {
                fetchUserApplications()
            }
        }
    }

    fun fetchUserApplications() {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
        if (email.isNotEmpty()) {
            repo.getUserApplications(email) { list ->
                _userApplications.clear()
                _userApplications.addAll(list)
            }
        }
    }

    // --- APPLICATION LOGIC (ADMIN SIDE) ---
    private val _allApplications = mutableStateListOf<ApplicationModel>()
    val allApplications: List<ApplicationModel> = _allApplications

    fun fetchAllApplications() {
        repo.getAllApplications { list ->
            _allApplications.clear()
            _allApplications.addAll(list)
            Log.d("FIREBASE_FETCH", "Admin fetched ${list.size} applications")
        }
    }

    /**
     * UPDATED: Fixed to ensure the UI locks immediately.
     * It updates the local state list so the "enabled" check in the
     * Compose UI triggers instantly.
     */
    fun updateApplicationStatus(applicationId: String, newStatus: String) {
        repo.updateApplicationStatus(applicationId, newStatus) { success ->
            if (success) {
                // Find the application in the local list and update it manually
                val index = _allApplications.indexOfFirst { it.applicationId == applicationId }
                if (index != -1) {
                    // Updating the item in the mutableStateListOf triggers recomposition
                    _allApplications[index] = _allApplications[index].copy(status = newStatus)
                }

                // Also update the User History list in case it's loaded
                val userIndex = _userApplications.indexOfFirst { it.applicationId == applicationId }
                if (userIndex != -1) {
                    _userApplications[userIndex] = _userApplications[userIndex].copy(status = newStatus)
                }

                Log.d("STATUS_UPDATE", "Successfully updated locally and on Firebase")
            }
        }
    }


    fun addJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        repo.addJob(model, callback)
    }

    fun deleteJob(jobId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteJob(jobId) { success, message ->
            if (success) fetchAllJobs()
            callback(success, message)
        }
    }

    fun updateJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        repo.updateJob(model) { success, message ->
            if (success) fetchAllJobs()
            callback(success, message)
        }
    }

    fun hasUserApplied(jobId: String): Boolean {
        return _userApplications.any { it.jobId == jobId }
    }

    fun clearAllData() {
        _savedJobIds.clear()
        _userApplications.clear()
        _allApplications.clear()
    }
}