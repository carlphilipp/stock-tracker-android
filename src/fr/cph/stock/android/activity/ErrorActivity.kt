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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.listener.ErrorButtonOnClickListener
import org.json.JSONException
import org.json.JSONObject

class ErrorActivity : Activity(), StockTrackerActivity {

    private lateinit var error: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.error)
        val msg = intent.getStringExtra("data")
        val login = intent.getStringExtra(Constants.LOGIN)
        val password = intent.getStringExtra(Constants.PASSWORD)
        try {
            val json = JSONObject(msg)
            error = findViewById(R.id.error_message)
            error.text = json.optString("error")
        } catch (e: JSONException) {
            Log.e(TAG, e.message, e)
        }

        val button = findViewById<Button>(R.id.retry_button)
        button.setOnClickListener(ErrorButtonOnClickListener(this, login, password))
    }

    override fun displayError(json: JSONObject) {
        error.text = json.optString("error")
    }

    override fun update(portfolio: Portfolio) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.PORTFOLIO, portfolio)
        finish()
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun logOut() {
        // no-op
    }

    companion object {
        private val TAG = "ErrorActivity"
    }
}
