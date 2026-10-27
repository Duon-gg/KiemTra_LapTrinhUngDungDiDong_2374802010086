package vn.edu.vlu.kiemtra2374802010086

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class ProfileActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ACCOUNT = "account"
        const val KEY_EMAIL = "email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val root = findViewById<android.view.View>(R.id.profileRoot)
        val padding = root.paddingLeft
        WindowInsetsControllerCompat(window, root).isAppearanceLightNavigationBars = true
        ViewCompat.setAccessibilityHeading(findViewById(R.id.tvProfileTitle), true)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(padding + bars.left, padding + bars.top, padding + bars.right, padding + bars.bottom)
            insets
        }
        val emailView = findViewById<TextView>(R.id.tvEmail)
        val logoutButton = findViewById<Button>(R.id.btnLogout)
        val account = intent.getBundleExtra(EXTRA_ACCOUNT)
        emailView.text = account?.getString(KEY_EMAIL).orEmpty()
        logoutButton.setOnClickListener { finish() }
    }
}
