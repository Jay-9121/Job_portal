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
    var type: String = "",
    var requirements: String = ""
) {
    /**
     * Converts the model into a Map for Firebase Database operations.
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "jobId" to jobId,
            "title" to title,
            "company" to company,
            "location" to location,
            "salary" to salary,
            "type" to type,
            "requirements" to requirements
        )
    }
}