package vn.edu.vlu.kiemtra2374802010086

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val root = findViewById<android.view.View>(R.id.loginRoot)
        val padding = root.paddingLeft
        WindowInsetsControllerCompat(window, root).isAppearanceLightNavigationBars = true
        ViewCompat.setAccessibilityHeading(findViewById(R.id.tvTitle), true)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(padding + bars.left, padding + bars.top, padding + bars.right, padding + bars.bottom)
            insets
        }
        val emailInput = findViewById<EditText>(R.id.etEmail)
        val passwordInput = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val status = findViewById<TextView>(R.id.tvStatus)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val error = when {
                email.isEmpty() && password.isEmpty() -> R.string.error_both_empty
                email.isEmpty() -> R.string.error_email_empty
                password.isEmpty() -> R.string.error_password_empty
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_email_invalid
                password.length < 6 -> R.string.error_password_short
                else -> null
            }
            status.setText(error ?: R.string.validation_success)
            status.setTextColor(getColor(if (error == null) R.color.success else R.color.error))
        }
    }
}
