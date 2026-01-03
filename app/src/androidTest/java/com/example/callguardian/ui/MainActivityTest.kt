package com.example.callguardian.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.example.callguardian.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun `main activity launches successfully`() {
        // Verify that the main activity is displayed
        onView(withId(R.id.main_scaffold)).check(matches(isDisplayed()))
    }

    @Test
    fun `app bar displays correct title`() {
        // Verify that the app bar shows the correct title
        onView(withId(R.id.top_app_bar)).check(matches(isDisplayed()))
        onView(withText("CallGuardian")).check(matches(isDisplayed()))
    }

    @Test
    fun `floating action button is displayed`() {
        // Verify that the FAB is displayed
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
    }

    @Test
    fun `floating action button is clickable`() {
        // Verify that the FAB can be clicked
        onView(withId(R.id.fab)).perform(click())
        // Note: This would need actual implementation to verify the action
    }

    @Test
    fun `navigation rail is displayed`() {
        // Verify that the navigation rail is displayed
        onView(withId(R.id.nav_rail)).check(matches(isDisplayed()))
    }

    @Test
    fun `navigation rail items are displayed`() {
        // Verify that navigation items are displayed
        onView(withText("Dashboard")).check(matches(isDisplayed()))
        onView(withText("History")).check(matches(isDisplayed()))
        onView(withText("Settings")).check(matches(isDisplayed()))
    }

    @Test
    fun `dashboard content is displayed by default`() {
        // Verify that dashboard content is shown by default
        onView(withId(R.id.dashboard_content)).check(matches(isDisplayed()))
    }

    @Test
    fun `switching to history tab works`() {
        // Click on History tab
        onView(withText("History")).perform(click())
        
        // Verify that history content is displayed
        onView(withId(R.id.history_content)).check(matches(isDisplayed()))
    }

    @Test
    fun `switching to settings tab works`() {
        // Click on Settings tab
        onView(withText("Settings")).perform(click())
        
        // Verify that settings content is displayed
        onView(withId(R.id.settings_content)).check(matches(isDisplayed()))
    }

    @Test
    fun `app displays correctly in different orientations`() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        
        // Test landscape orientation
        instrumentation.targetContext.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        
        onView(withId(R.id.main_scaffold)).check(matches(isDisplayed()))
        onView(withId(R.id.top_app_bar)).check(matches(isDisplayed()))
        
        // Test portrait orientation
        instrumentation.targetContext.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        
        onView(withId(R.id.main_scaffold)).check(matches(isDisplayed()))
        onView(withId(R.id.top_app_bar)).check(matches(isDisplayed()))
    }
}