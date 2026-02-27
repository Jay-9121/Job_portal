package com.example.job_portal.model

data class ApplicationModel(
    var applicationId: String = "",
    var jobId: String = "",
    var jobTitle: String = "",
    var userEmail: String = "",
    var cvDescription: String = "",
    var status: String = "Pending"
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "applicationId" to applicationId,
        "jobId" to jobId,
        "jobTitle" to jobTitle,
        "userEmail" to userEmail,
        "cvDescription" to cvDescription,
        "status" to status
    )
}