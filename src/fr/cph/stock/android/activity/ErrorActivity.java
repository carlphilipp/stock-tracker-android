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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import fr.cph.stock.android.R;
import fr.cph.stock.android.domain.Portfolio;
import fr.cph.stock.android.listener.ErrorButtonOnClickListener;

import static fr.cph.stock.android.Constants.LOGIN;
import static fr.cph.stock.android.Constants.PASSWORD;
import static fr.cph.stock.android.Constants.PORTFOLIO;

public class ErrorActivity extends Activity {
	private static final String TAG = "ErrorActivity";

	private TextView error;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error);
		final String msg = getIntent().getStringExtra("data");
		final String login = getIntent().getStringExtra(LOGIN);
		final String password = getIntent().getStringExtra(PASSWORD);
		try {
			JSONObject json = new JSONObject(msg);
			error = findViewById(R.id.error_message);
			error.setText(json.optString("error"));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Button button = findViewById(R.id.retry_button);
		button.setOnClickListener(new ErrorButtonOnClickListener(this, login, password));
	}

	public void displayError(final JSONObject json) {
		error.setText(json.optString("error"));
	}

	public void loadHome(final Portfolio portfolio) {
		final Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(PORTFOLIO, portfolio);
		finish();
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

}
