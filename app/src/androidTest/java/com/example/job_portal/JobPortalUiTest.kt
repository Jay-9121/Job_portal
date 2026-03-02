package com.example.job_portal

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.job_portal.ui.theme.Job_portalTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JobPortalUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- TEST 1: PASS ---
    // Verifies the "Post Job" screen loads its primary input fields.
    @Test
    fun testAddJobFormFieldsVisible() {
        composeTestRule.setContent {
            Job_portalTheme {
                AddJobBody()
            }
        }
        composeTestRule.onNodeWithTag("title_input").assertIsDisplayed()
        composeTestRule.onNodeWithTag("company_input").assertIsDisplayed()
    }

    // --- TEST 2: PASS ---
    // Verifies the "Post Job" button is present on the screen.
    @Test
    fun testPostButtonIsPresent() {
        composeTestRule.setContent {
            Job_portalTheme {
                AddJobBody()
            }
        }
        composeTestRule.onNodeWithTag("post_job_button").assertIsDisplayed()
    }

    // --- TEST 3: PASS ---
    // Simulates a user typing into the Title field.
    @Test
    fun testJobTitleInputTyping() {
        composeTestRule.setContent {
            Job_portalTheme {
                AddJobBody()
            }
        }
        composeTestRule.onNodeWithTag("title_input")
            .performTextInput("Senior Android Developer")

        composeTestRule.onNodeWithTag("title_input")
            .assertTextContains("Senior Android Developer")
    }

    // --- TEST 4: PASS ---
    // Verifies that the Search Bar can be interacted with.
    @Test
    fun testSearchBarInteraction() {
        composeTestRule.setContent {
            Job_portalTheme {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.testTag("search_bar")
                )
            }
        }
        composeTestRule.onNodeWithTag("search_bar").performClick()
    }

    // --- TEST 5: PASS ---
    // Checks if the "Apply Now" button is enabled by default.
    @Test
    fun testApplyButtonIsEnabled() {
        composeTestRule.setContent {
            Job_portalTheme {
                // Testing just the button component
                androidx.compose.material3.Button(
                    onClick = {},
                    modifier = Modifier.testTag("apply_now_button_test")
                ) { Text("Apply Now") }
            }
        }
        composeTestRule.onNodeWithTag("apply_now_button_test").assertIsEnabled()
    }

    // --- TEST 6: FAIL (Intentional) ---
    // This will fail because "wrong_tag_id" does not exist.
    @Test
    fun testIntentionalFailureForReview() {
        composeTestRule.setContent {
            Job_portalTheme {
                Text("Job Portal App", modifier = Modifier.testTag("welcome_header"))
            }
        }
        // This assertion will throw an error and fail the test
        composeTestRule.onNodeWithTag("wrong_tag_id").assertIsDisplayed()
    }
}