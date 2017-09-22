/**
 * Copyright 2013 Carl-Philipp Harmant
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.cph.stock.android;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONObject;

import fr.cph.stock.android.activity.ErrorActivity;
import fr.cph.stock.android.activity.LoginActivity;

/**
 * This class extends Application. It defines some functions that will be available anywhere within the app
 *
 * @author Carl-Philipp Harmant
 */
public class StockTrackerApp extends Application {

	/**
	 * This function logout the user and removes its login/password from the preferences. It also loads the login activity
	 *
	 * @param activity the activity to finish
	 */
	public void logOut(final Activity activity) {
		final SharedPreferences settings = getSharedPreferences(Constants.INSTANCE.getPREFS_NAME(), 0);
		final String login = settings.getString(Constants.INSTANCE.getLOGIN(), null);
		final String password = settings.getString(Constants.INSTANCE.getPASSWORD(), null);
		if (login != null) {
			settings.edit().remove(Constants.INSTANCE.getLOGIN()).apply();
		}
		if (password != null) {
			settings.edit().remove(Constants.INSTANCE.getPASSWORD()).apply();
		}
		activity.setResult(100);
		activity.finish();
		final Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * Check if a session is valid or not
	 *
	 * @param jsonObject the json session object to check
	 * @return true or false
	 */
	public boolean isSessionError(JSONObject jsonObject) {
		final String error = jsonObject.optString("error");
		return error.equals("No active session") || error.equals("User session not found");
	}

	/**
	 * This function loads the error activity to the screen. It happens usually when the session is timeout and needs to request a
	 * new session id to the server
	 *
	 * @param currentActivity the activity to stop
	 * @param jsonObject      the json object containing the error message
	 */
	public void loadErrorActivity(Activity currentActivity, JSONObject jsonObject) {
		Intent intent = new Intent(this, ErrorActivity.class);
		intent.putExtra("data", jsonObject.toString());
		SharedPreferences settings = getSharedPreferences(Constants.INSTANCE.getPREFS_NAME(), 0);
		String login = settings.getString(Constants.INSTANCE.getLOGIN(), null);
		String password = settings.getString(Constants.INSTANCE.getPASSWORD(), null);
		intent.putExtra(Constants.INSTANCE.getLOGIN(), login);
		intent.putExtra(Constants.INSTANCE.getPASSWORD(), password);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		currentActivity.finish();
	}

	/**
	 * This function toast a toast "updated" message to the screen
	 */
	public void toast() {
		Toast.makeText(getApplicationContext(), "Updated !", Toast.LENGTH_LONG).show();
	}
}
