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

data class ApplicationModel(
    val applicationId: String = "",
    val jobId: String = "",
    val jobTitle: String = "",
    val userEmail: String = "",
    val cvDescription: String = "",
    val status: String = "Pending"
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "applicationId" to applicationId,
            "jobId" to jobId,
            "jobTitle" to jobTitle,
            "userEmail" to userEmail,
            "cvDescription" to cvDescription,
            "status" to status
        )
    }
}