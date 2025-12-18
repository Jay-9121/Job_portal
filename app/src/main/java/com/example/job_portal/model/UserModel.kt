package com.example.job_portal.model

data class UserModel(
    val userId: String  = "",
    val email: String  = "",
    val firstName: String  = "",
    val lastName: String  = "",
    val phoneNumber: String = "",
    val location: String = "",
    val dob: String  = "",
    val gender: String  = "",
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "email" to email,
            "firstName" to firstName,
            "lastName" to lastName,
            "phoneNumber" to phoneNumber,
            "location" to location,
            "dob" to dob,
            "gender" to gender
        )
    }
}