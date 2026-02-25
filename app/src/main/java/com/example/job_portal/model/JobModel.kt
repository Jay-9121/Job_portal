package com.example.job_portal.model

/**
 * Model representing a Job posting.
 */
data class JobModel(
    var jobId: String = "",
    var title: String = "",
    var company: String = "",
    var location: String = "",
    var salary: String = "",
    var type: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "jobId" to jobId,
            "title" to title,
            "company" to company,
            "location" to location,
            "salary" to salary,
            "type" to type
        )
    }
}

