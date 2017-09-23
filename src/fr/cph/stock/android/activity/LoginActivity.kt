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
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask
import fr.cph.stock.android.web.Md5
import org.json.JSONObject
import java.util.*

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
class LoginActivity : Activity() {

    // Values for email and password at the time of the login attempt.
    private lateinit var login: String
    private lateinit var password: String

    // UI references.
    private var loginView: EditText? = null
    private var passwordView: EditText? = null
    private var checkBox: CheckBox? = null
    private var loginFormView: View? = null
    private var loginStatusView: View? = null
    private var loginStatusMessageView: TextView? = null
    private var errorView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "LoginActivity onCreate")
        setContentView(R.layout.login_activity)

        // Set up the login form.
        loginView = findViewById(R.id.email)
        errorView = findViewById(R.id.login_error)

        passwordView = findViewById(R.id.password)
        passwordView!!.setOnEditorActionListener { _, id, _ ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin()
            }
            false
        }

        checkBox = findViewById(R.id.checkbox)
        loginFormView = findViewById(R.id.login_form)
        loginStatusView = findViewById(R.id.login_status)
        loginStatusMessageView = findViewById(R.id.login_status_message)

        findViewById<View>(R.id.sign_in_button).setOnClickListener { _ -> attemptLogin() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        // to add minu top right
        // getMenuInflater().inflate(R.menu.login, menu);
        return true
    }

    /**
     * Attempts to sign in or register the account specified by the login form. If there are form errors
     * (invalid email, missing fields, etc.), the errors are presented and no actual login attempt is
     * made.
     */
    private fun attemptLogin() {

        // Reset errors.
        loginView!!.error = null
        passwordView!!.error = null

        // Store values at the time of the login attempt.
        login = loginView!!.text.toString()
        password = passwordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordView!!.error = getString(R.string.error_field_required)
            focusView = passwordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(login)) {
            loginView!!.error = getString(R.string.error_field_required)
            focusView = loginView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            loginStatusMessageView!!.setText(R.string.login_progress_signing_in)
            showProgress(true, null)
            val md5 = Md5(password)
            val params = object : HashMap<String, String>() {
                init {
                    put("login", login)
                    put("password", md5.hexInString)
                }
            }
            val derp = MainTask(this, UrlType.AUTH, params)
            derp.execute(null as Void?)
        }
    }

    private fun showProgress(show: Boolean, errorMessage: String?) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        loginStatusView!!.visibility = View.VISIBLE
        loginStatusView!!.animate().setDuration(shortAnimTime.toLong()).alpha((if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginStatusView!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        loginFormView!!.visibility = View.VISIBLE
        loginFormView!!.animate().setDuration(shortAnimTime.toLong()).alpha((if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
            }
        })
        if (!show) {
            errorView!!.text = errorMessage
            errorView!!.setTextColor(Color.rgb(160, 0, 0))
        }
    }

    fun onCheckboxClicked(view: View) {

    }

    fun loadHome(portfolio: Portfolio) {
        finish()
        if (checkBox!!.isChecked) {
            saveCredentials()
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.PORTFOLIO, portfolio)
        startActivity(intent)
    }

    private fun saveCredentials() {
        val settings = getSharedPreferences(Constants.PREFS_NAME, 0)
        settings.edit().putString(Constants.LOGIN, login).apply()
        val md5 = Md5(password)
        settings.edit().putString(Constants.PASSWORD, md5.hexInString).apply()
    }

    fun displayError(json: JSONObject) {
        showProgress(false, json.optString("error"))
    }

    companion object {
        private val TAG = "LoginActivity"
    }
}
