package com.example.job_portal.model

data class ApplicationModel(
    var applicationId: String = "",    // Change to var
    var jobId: String = "",            // Change to var
    var jobTitle: String = "",         // Change to var
    var userEmail: String = "",        // Change to var
    var cvDescription: String = "",    // Change to var
    var status: String = "Pending"      // Change to var
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