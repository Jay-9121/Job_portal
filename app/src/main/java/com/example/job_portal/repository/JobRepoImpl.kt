package com.example.job_portal.repository

import com.example.job_portal.model.ApplicationModel
import com.example.job_portal.model.JobModel
import com.google.firebase.database.*

class JobRepoImpl : JobRepo {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val jobsRef: DatabaseReference = database.getReference("jobs")
    private val savedJobsRef: DatabaseReference = database.getReference("SavedJobs")
    private val appRef: DatabaseReference = database.getReference("Applications")

    // --- JOB CRUD ---
    override fun addJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        val id = jobsRef.push().key ?: ""
        model.jobId = id
        jobsRef.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Job added")
            else callback(false, "${it.exception?.message}")
        }
    }

    override fun getAllJobs(callback: (Boolean, String, List<JobModel>?) -> Unit) {
        jobsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allJobs = snapshot.children.mapNotNull { it.getValue(JobModel::class.java) }
                callback(true, "Fetched", allJobs)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun updateJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        jobsRef.child(model.jobId).updateChildren(model.toMap()).addOnCompleteListener {
            callback(it.isSuccessful, if (it.isSuccessful) "Updated" else "Failed")
        }
    }

    override fun deleteJob(jobId: String, callback: (Boolean, String) -> Unit) {
        jobsRef.child(jobId).removeValue().addOnCompleteListener {
            callback(it.isSuccessful, if (it.isSuccessful) "Deleted" else "Failed")
        }
    }

    // --- SAVED JOBS ---
    override fun saveJobToDb(userId: String, jobId: String, callback: (Boolean) -> Unit) {
        savedJobsRef.child(userId).child(jobId).setValue(true).addOnCompleteListener { callback(it.isSuccessful) }
    }

    override fun unsaveJobFromDb(userId: String, jobId: String, callback: (Boolean) -> Unit) {
        savedJobsRef.child(userId).child(jobId).removeValue().addOnCompleteListener { callback(it.isSuccessful) }
    }

    override fun getSavedJobIds(userId: String, callback: (List<String>) -> Unit) {
        savedJobsRef.child(userId).get().addOnSuccessListener { snapshot ->
            val ids = snapshot.children.mapNotNull { it.key }
            callback(ids)
        }
    }

    // --- APPLICATION LOGIC ---
    override fun submitApplication(application: ApplicationModel, callback: (Boolean) -> Unit) {
        val id = appRef.push().key ?: ""
        val appWithId = application.copy(applicationId = id)
        appRef.child(id).setValue(appWithId).addOnCompleteListener { callback(it.isSuccessful) }
    }

    override fun getUserApplications(userEmail: String, callback: (List<ApplicationModel>) -> Unit) {
        // Querying applications where userEmail matches
        appRef.orderByChild("userEmail").equalTo(userEmail)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val apps = snapshot.children.mapNotNull { it.getValue(ApplicationModel::class.java) }
                    callback(apps)
                }
                override fun onCancelled(error: DatabaseError) { callback(emptyList()) }
            })
    }

    override fun getAllApplications(callback: (List<ApplicationModel>) -> Unit) {
        appRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val apps = snapshot.children.mapNotNull { it.getValue(ApplicationModel::class.java) }
                callback(apps)
            }
            override fun onCancelled(error: DatabaseError) { callback(emptyList()) }
        })
    }

    override fun updateApplicationStatus(applicationId: String, newStatus: String, callback: (Boolean) -> Unit) {
        appRef.child(applicationId).child("status").setValue(newStatus).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }
}