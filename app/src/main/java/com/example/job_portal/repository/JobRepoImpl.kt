package com.example.job_portal.repository

import com.example.job_portal.model.JobModel
import com.google.firebase.database.*

class JobRepoImpl : JobRepo {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.getReference("jobs")

    override fun addJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        model.jobId = id
        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Job added")
            else callback(false, "${it.exception?.message}")
        }
    }

    override fun getAllJobs(callback: (Boolean, String, List<JobModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allJobs = mutableListOf<JobModel>()
                for (data in snapshot.children) {
                    val job = data.getValue(JobModel::class.java)
                    if (job != null) allJobs.add(job)
                }
                callback(true, "Fetched", allJobs)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    // THIS IS WHAT WAS MISSING AND CAUSING THE ERROR:
    override fun updateJob(model: JobModel, callback: (Boolean, String) -> Unit) {
        ref.child(model.jobId).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Job updated")
            else callback(false, "${it.exception?.message}")
        }
    }

    override fun deleteJob(jobId: String, callback: (Boolean, String) -> Unit) {
        ref.child(jobId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Job deleted")
            else callback(false, "${it.exception?.message}")
        }
    }
}