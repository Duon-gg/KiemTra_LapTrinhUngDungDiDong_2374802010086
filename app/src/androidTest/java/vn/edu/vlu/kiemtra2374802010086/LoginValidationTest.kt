package vn.edu.vlu.kiemtra2374802010086

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginValidationTest {
    private fun check(email: String, password: String, message: String, screenshot: String? = null) {
        ActivityScenario.launch(LoginActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                activity.findViewById<EditText>(R.id.etEmail).setText(email)
                activity.findViewById<EditText>(R.id.etPassword).setText(password)
                activity.findViewById<Button>(R.id.btnLogin).performClick()
                assertEquals(message, activity.findViewById<TextView>(R.id.tvStatus).text.toString())
            }
            if (screenshot != null) Evidence.capture(screenshot)
        }
    }

    @Test fun emptyFieldsShowDetailedError() = check("", "", "Vui lòng nhập Email và mật khẩu.")
    @Test fun emptyEmailIsRejected() = check("   ", "123456", "Vui lòng nhập Email.")
    @Test fun emptyPasswordIsRejected() = check("student@example.com", "", "Vui lòng nhập mật khẩu.")
    @Test fun invalidEmailIsRejected() = check("email-sai", "123456", "Email không đúng định dạng.", "cau1_validation_error.png")
    @Test fun fiveCharacterPasswordIsRejected() = check("student@example.com", "12345", "Mật khẩu phải có ít nhất 6 ký tự.")
}
