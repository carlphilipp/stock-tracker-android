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
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask
import org.json.JSONObject
import java.util.*

/**
 * This class represents the base activity of the app
 *
 * @author Carl-Philipp Harmant
 */
class BaseActivity : Activity() {
    private lateinit var loginStatusView: View
    private lateinit var login: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "BaseActivity onCreate")
        setContentView(R.layout.loading)
        loginStatusView = findViewById(R.id.login_status)
        val settings = getSharedPreferences(Constants.PREFS_NAME, 0)

        if (settings.contains(Constants.LOGIN) && settings.contains(Constants.PASSWORD)) {
            showProgress(true)
            login = settings.getString(Constants.LOGIN, null)
            password = settings.getString(Constants.PASSWORD, null)
            val params = object : HashMap<String, String>() {
                init {
                    put("login", login)
                    put("password", password)
                }
            }
            val main = MainTask(this, UrlType.AUTH, params)
            main.execute(null as Void?)
        } else {
            showProgress(false)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Show progress bar
     *
     * @param show         show the bar or not
     * @param errorMessage the error message
     */
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        loginStatusView.visibility = View.VISIBLE
        loginStatusView.animate().setDuration(shortAnimTime.toLong()).alpha((if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginStatusView.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    /**
     * Load home
     *
     * @param portfolio the portfolio
     */
    fun loadHome(portfolio: Portfolio) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.PORTFOLIO, portfolio)
        showProgress(false)
        finish()
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * Display error
     *
     * @param jsonObject the json object
     */
    fun displayError(jsonObject: JSONObject) {
        val intent = Intent(this, ErrorActivity::class.java)
        intent.putExtra("data", jsonObject.toString())
        intent.putExtra(Constants.LOGIN, login)
        intent.putExtra(Constants.PASSWORD, password)
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = "Base"
    }
}
