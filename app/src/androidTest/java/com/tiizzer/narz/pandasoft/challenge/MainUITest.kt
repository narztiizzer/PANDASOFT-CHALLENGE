package com.tiizzer.narz.pandasoft.challenge

import android.app.PendingIntent.getActivity
import android.content.Intent.*
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.tiizzer.narz.pandasoft.challenge.main.MainActivity
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainUITest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, true, true)

    private val username = "usertest"
    private val password = "1234"

    private val PACKAGE_NAME = "com.tiizzer.narz.pandasoft.challenge"

    @Test
    fun testHandleView(){
        this.activityRule.activity.openLoginPage()

        onView(withId(R.id.username))
            .check(matches(isDisplayed()))

        onView(withId(R.id.password))
            .check(matches(isDisplayed()))

        onView(withId(R.id.button))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testUsernameInput() {
        this.activityRule.activity.openLoginPage()

        onView(withId(R.id.button)).perform(click())
        onView(withText(R.string.username_problem_message))
            .inRoot(
                withDecorView(
                    not(
                        `is`(this.activityRule.activity.window.decorView)
                    )
                )
            ).check(matches(isDisplayed()))
    }

    @Test
    fun testPasswordInput() {
        this.activityRule.activity.openLoginPage()

        onView(withId(R.id.username)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())
        onView(withText(R.string.password_problem_message))
            .inRoot(
                withDecorView(
                    not(
                        `is`(this.activityRule.activity.window.decorView)
                    )
                )
            ).check(matches(isDisplayed()))
    }
}