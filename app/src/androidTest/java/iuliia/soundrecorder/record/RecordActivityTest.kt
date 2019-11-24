package iuliia.soundrecorder.record

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.rule.ActivityTestRule
import iuliia.soundrecorder.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordActivityTest {
    @get:Rule
    val rule = ActivityTestRule(RecordActivity::class.java)

    @Test
    fun buttonsVisible() {
        onView(withId(R.id.stopRecordingButton)).perform(click())
        onView(withId(R.id.startRecordingButton)).check(matches(isDisplayed()))
        onView(withId(R.id.startRecordingButton)).check(matches(isEnabled()))
        onView(withId(R.id.stopRecordingButton)).check(matches(isDisplayed()))
        onView(withId(R.id.stopRecordingButton)).check(matches(not(isEnabled())))
    }
}