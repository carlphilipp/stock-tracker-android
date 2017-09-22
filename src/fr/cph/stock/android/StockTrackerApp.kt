/**
 * Copyright 2013 Carl-Philipp Harmant
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

package fr.cph.stock.android

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast

import org.json.JSONObject

import fr.cph.stock.android.activity.ErrorActivity
import fr.cph.stock.android.activity.LoginActivity

/**
 * This class extends Application. It defines some functions that will be available anywhere within the app
 *
 * @author Carl-Philipp Harmant
 */
class StockTrackerApp : Application() {

    /**
     * This function logout the user and removes its login/password from the preferences. It also loads the login activity
     *
     * @param activity the activity to finish
     */
    fun logOut(activity: Activity) {
        val settings = getSharedPreferences(Constants.PREFS_NAME, 0)
        val login = settings.getString(Constants.LOGIN, null)
        val password = settings.getString(Constants.PASSWORD, null)
        if (login != null) {
            settings.edit().remove(Constants.LOGIN).apply()
        }
        if (password != null) {
            settings.edit().remove(Constants.PASSWORD).apply()
        }
        activity.setResult(100)
        activity.finish()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /**
     * Check if a session is valid or not
     *
     * @param jsonObject the json session object to check
     * @return true or false
     */
    fun isSessionError(jsonObject: JSONObject): Boolean {
        val error = jsonObject.optString("error")
        return error == "No active session" || error == "User session not found"
    }

    /**
     * This function loads the error activity to the screen. It happens usually when the session is timeout and needs to request a
     * new session id to the server
     *
     * @param currentActivity the activity to stop
     * @param jsonObject      the json object containing the error message
     */
    fun loadErrorActivity(currentActivity: Activity, jsonObject: JSONObject) {
        val intent = Intent(this, ErrorActivity::class.java)
        intent.putExtra("data", jsonObject.toString())
        val settings = getSharedPreferences(Constants.PREFS_NAME, 0)
        val login = settings.getString(Constants.LOGIN, null)
        val password = settings.getString(Constants.PASSWORD, null)
        intent.putExtra(Constants.LOGIN, login)
        intent.putExtra(Constants.PASSWORD, password)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        currentActivity.finish()
    }

    /**
     * This function toast a toast "updated" message to the screen
     */
    fun toast() {
        Toast.makeText(applicationContext, "Updated !", Toast.LENGTH_LONG).show()
    }
}
