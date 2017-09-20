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

package fr.cph.stock.android.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import java.util.Map;

import fr.cph.stock.android.client.Client;
import fr.cph.stock.android.entity.Portfolio;
import fr.cph.stock.android.entity.ResponseDTO;
import fr.cph.stock.android.enumtype.UrlType;
import fr.cph.stock.android.exception.AppException;
import fr.cph.stock.android.util.UserContext;

public class MainTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = "MainTask";

	private final Object object;
	private final UrlType urlType;
	private final Map<String, String> params;
	private ResponseDTO responseDTO;
	private String error;

	//FIXME logout does not seem to be working
	public MainTask(final Object object, final UrlType urlType, @NonNull final Map<String, String> params) {
		this.object = object;
		this.urlType = urlType;
		this.params = params;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		boolean toReturn = true;
		try {
			responseDTO = Client.getInstance().getResponse(urlType, this.params);
		} catch (final AppException e) {
			Log.e(TAG, e.getMessage(), e);
			this.error = e.getMessage();
			toReturn = false;
		}
		if (responseDTO != null) {
			final String errorMessage = responseDTO.getError();
			if (errorMessage != null && !errorMessage.equals("")) {
				this.error = errorMessage;
				toReturn = false;
			}
		}
		return toReturn;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		try {
			Class<?> clazz = object.getClass();
			Class<?>[] param;
			if (success) {
				UserContext.setup(responseDTO.getPortfolio().getLocale());
				switch (urlType) {
					case LOGOUT:
						Log.d(TAG, "logout: " + clazz.getName());
						clazz.getMethod("logOut").invoke(object);
						break;
					case UPDATEHISTORY:
						param = new Class[1];
						param[0] = Portfolio.class;
						clazz.getMethod("reloadData", param).invoke(object, responseDTO.getPortfolio());
						break;
					case AUTH:
						param = new Class[1];
						param[0] = Portfolio.class;
						clazz.getMethod("loadHome", param).invoke(object, responseDTO.getPortfolio());
						break;
					case RELOAD:
						param = new Class[1];
						param[0] = Portfolio.class;
						clazz.getMethod("reloadData", param).invoke(object, responseDTO.getPortfolio());
						break;
				}
			} else {
				param = new Class[1];
				param[0] = JSONObject.class;
				final JSONObject jsonError = new JSONObject();
				jsonError.accumulate("error", error);
				clazz.getMethod("displayError", param).invoke(object, jsonError);
			}
		} catch (final Exception e) {
			Log.e(TAG, "", e);
		}
		super.onPostExecute(success);
	}
}
