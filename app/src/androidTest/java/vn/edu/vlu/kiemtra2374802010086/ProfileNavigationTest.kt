package vn.edu.vlu.kiemtra2374802010086

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileNavigationTest {
    private fun loginAndLogout(email: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val monitor = instrumentation.addMonitor(ProfileActivity::class.java.name, null, false)
        try {
            ActivityScenario.launch(LoginActivity::class.java).use { scenario ->
                scenario.onActivity { activity ->
                    activity.findViewById<EditText>(R.id.etEmail).setText(email)
                    activity.findViewById<EditText>(R.id.etPassword).setText("123456")
                    activity.findViewById<Button>(R.id.btnLogin).performClick()
                }
                val profile = monitor.waitForActivityWithTimeout(5000)
                assertNotNull("Đăng nhập hợp lệ phải mở ProfileActivity", profile)
                try {
                    instrumentation.waitForIdleSync()
                    instrumentation.runOnMainSync {
                        assertEquals(email.trim(), profile.findViewById<TextView>(R.id.tvEmail).text.toString())
                    }
                    instrumentation.runOnMainSync { profile.findViewById<Button>(R.id.btnLogout).performClick() }
                    instrumentation.waitForIdleSync()
                    assertTrue("Đăng xuất phải đóng ProfileActivity", profile.isFinishing)
                    val deadline = SystemClock.uptimeMillis() + 5000
                    while (scenario.state != Lifecycle.State.RESUMED && SystemClock.uptimeMillis() < deadline) {
                        SystemClock.sleep(50)
                    }
                    assertEquals(Lifecycle.State.RESUMED, scenario.state)
                    scenario.onActivity { activity ->
                        assertEquals(email, activity.findViewById<EditText>(R.id.etEmail).text.toString())
                    }
                } finally {
                    instrumentation.runOnMainSync { profile.finish() }
                }
            }
        } finally {
            instrumentation.removeMonitor(monitor)
        }
    }

    @Test fun sixCharacterPasswordNavigatesAndLogoutReturns() =
        loginAndLogout("student@example.com")

    @Test fun surroundingEmailWhitespaceIsTrimmedWhenPassed() =
        loginAndLogout("  another@example.org  ")

    @Test fun profileKeepsEmailAfterRecreation() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val bundle = Bundle().apply { putString(ProfileActivity.KEY_EMAIL, "restore@example.com") }
        val intent = Intent(context, ProfileActivity::class.java).putExtra(ProfileActivity.EXTRA_ACCOUNT, bundle)
        ActivityScenario.launch<ProfileActivity>(intent).use { scenario ->
            scenario.recreate()
            scenario.onActivity { activity ->
                assertEquals("restore@example.com", activity.findViewById<TextView>(R.id.tvEmail).text.toString())
            }
        }
    }
}
