/**
 * Copyright 2017 Carl-Philipp Harmant
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.cph.stock.android.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import fr.cph.stock.android.Constants.LOGIN
import fr.cph.stock.android.Constants.PASSWORD
import fr.cph.stock.android.Constants.PORTFOLIO
import fr.cph.stock.android.Constants.PREFS_NAME
import fr.cph.stock.android.Constants.RED
import fr.cph.stock.android.R
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask
import fr.cph.stock.android.web.Md5
import org.json.JSONObject

/**
 * Activity which displays a login screen to the user.
 */
class LoginActivity : Activity(), StockTrackerActivity {

    private lateinit var login: String
    private lateinit var password: String

    // UI references.
    private lateinit var loginView: EditText
    private lateinit var passwordView: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var loginFormView: View
    private lateinit var loginStatusView: View
    private lateinit var loginStatusMessageView: TextView
    private lateinit var errorView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        loginView = findViewById(R.id.email)
        errorView = findViewById(R.id.login_error)
        passwordView = findViewById(R.id.password)
        checkBox = findViewById(R.id.checkbox)
        loginFormView = findViewById(R.id.login_form)
        loginStatusView = findViewById(R.id.login_status)
        loginStatusMessageView = findViewById(R.id.login_status_message)
        findViewById<View>(R.id.sign_in_button).setOnClickListener { _ -> attemptLogin() }
    }

    private fun attemptLogin() {

        // Reset errors.
        loginView.error = null
        passwordView.error = null

        // Store values at the time of the login attempt.
        login = loginView.text.toString()
        password = passwordView.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordView.error = getString(R.string.error_field_required)
            focusView = passwordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(login)) {
            loginView.error = getString(R.string.error_field_required)
            focusView = loginView
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        } else {
            loginStatusMessageView.setText(R.string.login_progress_signing_in)
            showProgress(true, null)
            val params = hashMapOf(LOGIN to login, PASSWORD to Md5(password).hexInString)
            MainTask(this, UrlType.AUTH, params).execute()
        }
    }

    override fun update(portfolio: Portfolio) {
        finish()
        if (checkBox.isChecked) {
            saveCredentials()
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(PORTFOLIO, portfolio)
        startActivity(intent)
    }

    override fun displayError(json: JSONObject) {
        showProgress(false, json.optString("error"))
    }

    private fun showProgress(show: Boolean, errorMessage: String?) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        loginStatusView.visibility = View.VISIBLE
        loginStatusView.animate().setDuration(shortAnimTime.toLong()).alpha((if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginStatusView.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        loginFormView.visibility = View.VISIBLE
        loginFormView.animate().setDuration(shortAnimTime.toLong()).alpha((if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginFormView.visibility = if (show) View.GONE else View.VISIBLE
            }
        })
        if (!show) {
            errorView.text = errorMessage
            errorView.setTextColor(RED)
        }
    }

    private fun saveCredentials() {
        val settings = getSharedPreferences(PREFS_NAME, 0)
        settings.edit().putString(LOGIN, login).apply()
        settings.edit().putString(PASSWORD, Md5(password).hexInString).apply()
    }

    override fun logOut() {
        // no-op
    }

    companion object {
        private val TAG = "LoginActivity"
    }
}
