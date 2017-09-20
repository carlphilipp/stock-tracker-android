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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;

import fr.cph.stock.android.R;
import fr.cph.stock.android.StockTrackerApp;
import fr.cph.stock.android.entity.Portfolio;
import fr.cph.stock.android.enumtype.ChartType;
import fr.cph.stock.android.enumtype.UrlType;
import fr.cph.stock.android.task.MainTask;
import fr.cph.stock.android.web.DebugWebChromeClient;

import static fr.cph.stock.android.Constants.PORTFOLIO;

/**
 * This class reprents the chart activity
 *
 * @author Carl-Philipp Harmant
 */
public class ChartActivity extends Activity implements IStockTrackerActivity {

	private static final String TAG = "ChartActivity";

	/**
	 * Graphics components
	 **/
	private MenuItem menuItem;
	private TextView errorView;
	private ChartType chartType;
	private Portfolio portfolio;
	private WebView webView;
	private ActionBar actionBar;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_activity);

		portfolio = getIntent().getParcelableExtra(PORTFOLIO);
		chartType = ChartType.getEnum(getIntent().getStringExtra("chartType"));

		errorView = findViewById(R.id.errorMessage);
		actionBar = getActionBar();
		webView = findViewById(R.id.webView);
		String data = getData();
		webView.setWebChromeClient(new DebugWebChromeClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// myWebView.setBackgroundColor(0x00000000);
		// myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		webView.loadDataWithBaseURL("file:///android_asset/www/", data, "text/html", "UTF-8", null);
		webView.reload();
	}

	private String getData() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		String data = null;
		InputStream is = null;
		try {
			StringWriter writer = new StringWriter();
			switch (chartType) {
				case CAPITALIZATION:
					is = getApplicationContext().getAssets().open("www/pie.html");
					IOUtils.copy(is, writer, "UTF8");
					data = writer.toString();
					data = data.replace("#DATA#", portfolio.getChartCapData());
					data = data.replace("#TITLE#", portfolio.getChartCapTitle());
					data = data.replace("#DRAW#", portfolio.getChartCapDraw());
					data = data.replace("#COMPANIES#", portfolio.getChartCapCompanies());
					data = data.replaceAll("#WIDTH#", (int) (metrics.widthPixels / metrics.density) - 30 + "");
					data = data.replaceAll("#HEIGHT#", (int) (metrics.widthPixels / metrics.density) - 30 + "");
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					actionBar.setTitle("Capitalization Chart");
					webView.setHorizontalScrollBarEnabled(false);
					break;
				case SECTOR:
					is = getApplicationContext().getAssets().open("www/pie.html");
					IOUtils.copy(is, writer, "UTF8");
					data = writer.toString();
					data = data.replace("#DATA#", portfolio.getChartSectorData());
					data = data.replace("#TITLE#", portfolio.getChartSectorTitle());
					data = data.replace("#DRAW#", portfolio.getChartSectorDraw());
					data = data.replace("#COMPANIES#", portfolio.getChartSectorCompanies());
					data = data.replaceAll("#WIDTH#", (int) (metrics.widthPixels / metrics.density) - 30 + "");
					data = data.replaceAll("#HEIGHT#", (int) (metrics.widthPixels / metrics.density) - 30 + "");
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					actionBar.setTitle("Sector Chart");
					webView.setHorizontalScrollBarEnabled(false);
					break;
				case SHARE_VALUE:
					is = getApplicationContext().getAssets().open("www/share_value.html");
					IOUtils.copy(is, writer, "UTF8");
					data = writer.toString();
					data = data.replace("#DATA#", portfolio.getChartData());
					data = data.replace("#DRAW#", portfolio.getChartDraw());
					data = data.replace("#COLOR#", portfolio.getChartColors());
					data = data.replace("#DATE#", portfolio.getChartDate());
					data = data.replaceAll("#WIDTH#", ((int) (metrics.widthPixels / metrics.density)) - 30 + "");
					data = data.replaceAll("#HEIGHT#", (int) (metrics.heightPixels / metrics.density / 1.35) + "");
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					actionBar.setTitle("Share Value Chart");
					break;
			}
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}finally {
			IOUtils.closeQuietly(is);
		}
		return data;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MainTask mainTask;
		switch (item.getItemId()) {
			case R.id.action_logout:
				mainTask = new MainTask(this, UrlType.LOGOUT, Collections.emptyMap());
				mainTask.execute((Void) null);
				return true;
			case R.id.refresh:
				menuItem = item;
				menuItem.setActionView(R.layout.progressbar);
				menuItem.expandActionView();
				mainTask = new MainTask(this, UrlType.RELOAD, Collections.emptyMap());
				mainTask.execute((Void) null);
				return true;
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void reloadData(Portfolio portfolio) {
		menuItem.collapseActionView();
		menuItem.setActionView(null);
		Intent resultIntent = new Intent();
		resultIntent.putExtra(PORTFOLIO, portfolio);
		this.portfolio = portfolio;
		String data = getData();
		webView.loadDataWithBaseURL("file:///android_asset/www/", data, "text/html", "UTF-8", null);
		setResult(Activity.RESULT_OK, resultIntent);
		StockTrackerApp app = (StockTrackerApp) getApplication();
		app.toast();
	}

	@Override
	public void displayError(JSONObject json) {
		boolean sessionError = ((StockTrackerApp) getApplication()).isSessionError(json);
		if (sessionError) {
			((StockTrackerApp) getApplication()).loadErrorActivity(this, json);
		} else {
			errorView.setText(json.optString("error"));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.BELOW, errorView.getId());
			WebView webView = findViewById(R.id.webView);
			webView.setLayoutParams(params);
			menuItem.collapseActionView();
			menuItem.setActionView(null);
		}
	}

	@Override
	public void logOut() {
		((StockTrackerApp) getApplication()).logOut(this);
	}
}
