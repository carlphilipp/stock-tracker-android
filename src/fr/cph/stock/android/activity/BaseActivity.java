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

package fr.cph.stock.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.cph.stock.android.Constants;
import fr.cph.stock.android.R;
import fr.cph.stock.android.domain.Portfolio;
import fr.cph.stock.android.domain.UrlType;
import fr.cph.stock.android.task.MainTask;

/**
 * This class represents the base activity of the app
 *
 * @author Carl-Philipp Harmant
 */
public class BaseActivity extends Activity {

	private static final String TAG = "Base";
	private View mLoginStatusView;
	private String login;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "BaseActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		mLoginStatusView = findViewById(R.id.login_status);
		SharedPreferences settings = getSharedPreferences(Constants.INSTANCE.getPREFS_NAME(), 0);

		if (settings.contains(Constants.INSTANCE.getLOGIN()) && settings.contains(Constants.INSTANCE.getPASSWORD())) {
			showProgress(true, null);
			login = settings.getString(Constants.INSTANCE.getLOGIN(), null);
			password = settings.getString(Constants.INSTANCE.getPASSWORD(), null);
			final Map<String, String> params = new HashMap<String, String>() {{
				put("login", login);
				put("password", password);
			}};
			MainTask main = new MainTask(this, UrlType.AUTH, params);
			main.execute((Void) null);
		} else {
			showProgress(false, null);
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * Show progress bar
	 *
	 * @param show         show the bar or not
	 * @param errorMessage the error message
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show, String errorMessage) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
		mLoginStatusView.setVisibility(View.VISIBLE);
		mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			}
		});
	}

	/**
	 * Load home
	 *
	 * @param portfolio the portfolio
	 */
	public void loadHome(Portfolio portfolio) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Constants.INSTANCE.getPORTFOLIO(), portfolio);
		showProgress(false, null);
		finish();
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	/**
	 * Display error
	 *
	 * @param jsonObject the json object
	 */
	public void displayError(JSONObject jsonObject) {
		Intent intent = new Intent(this, ErrorActivity.class);
		intent.putExtra("data", jsonObject.toString());
		intent.putExtra(Constants.INSTANCE.getLOGIN(), login);
		intent.putExtra(Constants.INSTANCE.getPASSWORD(), password);
		startActivity(intent);
		finish();
	}
}
