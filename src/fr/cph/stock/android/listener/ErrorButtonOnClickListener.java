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

package fr.cph.stock.android.listener;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fr.cph.stock.android.Constants;
import fr.cph.stock.android.activity.ErrorActivity;
import fr.cph.stock.android.domain.UrlType;
import fr.cph.stock.android.task.MainTask;

public class ErrorButtonOnClickListener implements View.OnClickListener {

	private final ErrorActivity errorActivity;
	private final String login;
	private final String password;

	public ErrorButtonOnClickListener(ErrorActivity errorActivity, String login, String password) {
		this.errorActivity = errorActivity;
		this.login = login;
		this.password = password;
	}

	@Override
	public void onClick(final View v) {
		final Map<String, String> params = new HashMap<String, String>() {{
			put(Constants.INSTANCE.getURL_LOGIN(), login);
			put(Constants.INSTANCE.getURL_PASSWORD(), password);
		}};
		final MainTask mainTask = new MainTask(errorActivity, UrlType.AUTH, params);
		mainTask.execute((Void) null);
	}

}
