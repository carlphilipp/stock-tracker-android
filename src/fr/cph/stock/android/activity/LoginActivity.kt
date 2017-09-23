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
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

import org.json.JSONObject

import java.util.HashMap

import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask
import fr.cph.stock.android.web.Md5

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
class LoginActivity : Activity() {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private var mLogin: String? = null
    private var mPassword: String? = null

    // UI references.
    private var mLoginView: EditText? = null
    private var mPasswordView: EditText? = null
    private var checkBox: CheckBox? = null
    private var mLoginFormView: View? = null
    private var mLoginStatusView: View? = null
    private var mLoginStatusMessageView: TextView? = null
    private var errorView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "LoginActivity onCreate")
        super.onCreate(savedInstanceState)
        // Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity)

        // Set up the login form.
        mLoginView = findViewById(R.id.email)
        errorView = findViewById(R.id.login_error)

        mPasswordView = findViewById(R.id.password)
        mPasswordView!!.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin()
            }
            false
        }

        checkBox = findViewById(R.id.checkbox)
        mLoginFormView = findViewById(R.id.login_form)
        mLoginStatusView = findViewById(R.id.login_status)
        mLoginStatusMessageView = findViewById(R.id.login_status_message)

        findViewById<View>(R.id.sign_in_button).setOnClickListener { view -> attemptLogin() }
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
    fun attemptLogin() {
        // if (mAuthTask != null) {
        // return;
        // }

        // Reset errors.
        mLoginView!!.error = null
        mPasswordView!!.error = null

        // Store values at the time of the login attempt.
        mLogin = mLoginView!!.text.toString()
        mPassword = mPasswordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView!!.error = getString(R.string.error_field_required)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mLogin)) {
            mLoginView!!.error = getString(R.string.error_field_required)
            focusView = mLoginView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView!!.setText(R.string.login_progress_signing_in)
            showProgress(true, null)
            val md5 = Md5(mPassword!!)
            val params = object : HashMap<String, String>() {
                init {
                    put("login", mLogin!!)
                    put("password", md5.hexInString)
                }
            }
            val derp = MainTask(this, UrlType.AUTH, params)
            derp.execute(null as Void?)
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean, errorMessage: String?) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        mLoginStatusView!!.visibility = View.VISIBLE
        mLoginStatusView!!.animate().setDuration(shortAnimTime.toLong()).alpha((if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mLoginStatusView!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        mLoginFormView!!.visibility = View.VISIBLE
        mLoginFormView!!.animate().setDuration(shortAnimTime.toLong()).alpha((if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
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
        settings.edit().putString(Constants.LOGIN, mLogin).apply()
        val md5 = Md5(mPassword!!)
        settings.edit().putString(Constants.PASSWORD, md5.hexInString).apply()
    }

    fun displayError(json: JSONObject) {
        showProgress(false, json.optString("error"))
    }

    companion object {

        private val TAG = "LoginActivity"
    }
}
