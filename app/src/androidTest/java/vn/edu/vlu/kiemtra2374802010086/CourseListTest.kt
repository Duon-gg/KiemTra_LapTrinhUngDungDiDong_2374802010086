package vn.edu.vlu.kiemtra2374802010086

import android.content.Intent
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.widget.ListView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CourseListTest {
    private val courses = listOf(
        "Lập trình Kotlin",
        "Thiết kế Layout XML",
        "Quản lý Git/GitHub",
        "Xử lý Intent",
        "Kiến trúc Android"
    )

    private fun launchProfile(): ActivityScenario<ProfileActivity> {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val account = Bundle().apply { putString(ProfileActivity.KEY_EMAIL, "student@example.com") }
        return ActivityScenario.launch(
            Intent(context, ProfileActivity::class.java).putExtra(ProfileActivity.EXTRA_ACCOUNT, account)
        )
    }

    @Test fun allFiveCoursesAreDisplayedInOrder() {
        launchProfile().use { scenario ->
            scenario.onActivity { activity ->
                val list = activity.findViewById<ListView>(R.id.lvCourses)
                assertNotNull("ListView phải có Adapter", list.adapter)
                assertEquals(courses.size, list.count)
                courses.forEachIndexed { index, name -> assertEquals(name, list.getItemAtPosition(index)) }
            }
            Evidence.capture("cau3_listview_display.png")
        }
    }

    private fun checkToast(position: Int, screenshot: String? = null) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        launchProfile().use { scenario ->
            val event = instrumentation.uiAutomation.executeAndWaitForEvent(
                {
                    scenario.onActivity { activity ->
                        val list = activity.findViewById<ListView>(R.id.lvCourses)
                        assertNotNull("ListView phải có Adapter", list.adapter)
                        val row = list.getChildAt(position - list.firstVisiblePosition)
                        assertNotNull("Phần tử phải hiển thị để bấm", row)
                        list.performItemClick(row, position, list.getItemIdAtPosition(position))
                    }
                },
                { event ->
                    event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED &&
                        event.text.any { it.toString() == courses[position] }
                },
                5000
            )
            assertTrue(event.text.any { it.toString() == courses[position] })
            if (screenshot != null) Evidence.capture(screenshot)
        }
    }

    @Test fun firstItemToastMatchesSelectedCourse() = checkToast(0, "cau3_item_click_toast.png")
    @Test fun lastItemToastMatchesSelectedCourse() = checkToast(4)
}
