package com.example.job_portal

import com.example.job_portal.model.JobModel
import com.example.job_portal.repository.JobRepo
import com.example.job_portal.viewmodel.JobViewModel
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class JobViewModelTest {

    // Test 1: Success Scenario (Already there)
    @Test
    fun testAddJobSuccess() {
        val mockRepo = mock(JobRepo::class.java)
        val viewModel = JobViewModel(mockRepo)
        val dummyJob = JobModel(title = "Android Dev", company = "PathVista")

        whenever(mockRepo.addJob(any(), any())).thenAnswer {
            val callback = it.arguments[1] as (Boolean, String) -> Unit
            callback(true, "Success")
        }

        viewModel.addJob(dummyJob) { success, message ->
            assertTrue(success)
            assertEquals("Success", message)
        }
    }

    // Test 2: Failure Scenario (Add this now)
    @Test
    fun testAddJobFailure() {
        val mockRepo = mock(JobRepo::class.java)
        val viewModel = JobViewModel(mockRepo)
        val dummyJob = JobModel(title = "Android Dev", company = "PathVista")

        // Simulate a database error
        whenever(mockRepo.addJob(any(), any())).thenAnswer {
            val callback = it.arguments[1] as (Boolean, String) -> Unit
            callback(false, "Database Error")
        }

        viewModel.addJob(dummyJob) { success, message ->
            assertFalse(success)
            assertEquals("Database Error", message)
        }
    }
}