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

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import fr.cph.stock.android.Constants;
import fr.cph.stock.android.activity.ChartActivity;
import fr.cph.stock.android.activity.MainActivity;
import fr.cph.stock.android.domain.ChartType;
import fr.cph.stock.android.domain.Portfolio;

public class ChartListener implements OnClickListener {

	private final Portfolio portfolio;
	private final Activity activity;
	private final ChartType chartType;

	public ChartListener(final Activity activity, final Portfolio portfolio, final ChartType chartType) {
		this.activity = activity;
		this.portfolio = portfolio;
		this.chartType = chartType;
	}

	@Override
	public void onClick(final View v) {
		final Intent intent = new Intent(activity.getApplicationContext(), ChartActivity.class);
		intent.putExtra(Constants.INSTANCE.getPORTFOLIO(), portfolio);
		intent.putExtra("chartType", chartType.getValue());
		activity.startActivityForResult(intent, MainActivity.CHART_REQUEST);
		activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
}
