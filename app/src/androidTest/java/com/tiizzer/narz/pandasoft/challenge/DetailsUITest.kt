package com.tiizzer.narz.pandasoft.challenge

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.tiizzer.narz.pandasoft.challenge.details.DetailsActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsUITest {
    @get:Rule
    var activityRule: ActivityTestRule<DetailsActivity> = ActivityTestRule(DetailsActivity::class.java, true, true)

    @Before
    fun prepare(){
        this.activityRule.activity.prepareMockData()
    }

    @Test
    fun testContent(){
        onView(withId(R.id.content_title))
            .check(matches(ViewMatchers.withText("Hello, details page.")))

        onView(withId(R.id.content_details))
            .check(matches(ViewMatchers.withText("This is a content for detail page")))

        onView(withId(R.id.content_date))
            .check(matches(ViewMatchers.withText("19/02/2020")))
    }
}
