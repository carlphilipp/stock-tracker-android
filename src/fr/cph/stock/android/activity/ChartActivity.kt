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

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.RelativeLayout
import android.widget.TextView
import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.StockTrackerApp
import fr.cph.stock.android.domain.ChartType
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask
import fr.cph.stock.android.web.DebugWebChromeClient
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.StringWriter

/**
 * This class reprents the chart activity
 *
 * @author Carl-Philipp Harmant
 */
class ChartActivity : Activity(), IStockTrackerActivity {

    /**
     * Graphics components
     */
    private lateinit var menuItem: MenuItem
    private lateinit var errorView: TextView
    private lateinit var chartType: ChartType
    private lateinit var portfolio: Portfolio
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_activity)

        portfolio = intent.getParcelableExtra(Constants.PORTFOLIO)
        chartType = ChartType.getEnum(intent.getStringExtra("chartType"))

        errorView = findViewById(R.id.errorMessage)
        webView = findViewById(R.id.webView)
        val data = data
        webView.webChromeClient = DebugWebChromeClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        // myWebView.setBackgroundColor(0x00000000);
        // myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.loadDataWithBaseURL("file:///android_asset/www/", data, "text/html", "UTF-8", null)
        webView.reload()
    }

    private val data: String?
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            var data: String? = null
            var source: InputStream? = null
            try {
                val writer = StringWriter()
                when (chartType) {
                    ChartType.CAPITALIZATION -> {
                        source = applicationContext.assets.open("www/pie.html")
                        IOUtils.copy(source, writer, "UTF8")
                        data = writer.toString()
                        data = data.replace("#DATA#", portfolio.chartCapData!!)
                        data = data.replace("#TITLE#", portfolio.chartCapTitle!!)
                        data = data.replace("#DRAW#", portfolio.chartCapDraw!!)
                        data = data.replace("#COMPANIES#", portfolio.chartCapCompanies!!)
                        data = data.replace("#WIDTH#".toRegex(), ((metrics.widthPixels / metrics.density).toInt() - 30).toString() + "")
                        data = data.replace("#HEIGHT#".toRegex(), ((metrics.widthPixels / metrics.density).toInt() - 30).toString() + "")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        actionBar.title = "Capitalization Chart"
                        webView.isHorizontalScrollBarEnabled = false
                    }
                    ChartType.SECTOR -> {
                        source = applicationContext.assets.open("www/pie.html")
                        IOUtils.copy(source, writer, "UTF8")
                        data = writer.toString()
                        data = data.replace("#DATA#", portfolio.chartSectorData!!)
                        data = data.replace("#TITLE#", portfolio.chartSectorTitle!!)
                        data = data.replace("#DRAW#", portfolio.chartSectorDraw!!)
                        data = data.replace("#COMPANIES#", portfolio.chartSectorCompanies!!)
                        data = data.replace("#WIDTH#".toRegex(), ((metrics.widthPixels / metrics.density).toInt() - 30).toString() + "")
                        data = data.replace("#HEIGHT#".toRegex(), ((metrics.widthPixels / metrics.density).toInt() - 30).toString() + "")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        actionBar.title = "Sector Chart"
                        webView.isHorizontalScrollBarEnabled = false
                    }
                    ChartType.SHARE_VALUE -> {
                        source = applicationContext.assets.open("www/share_value.html")
                        IOUtils.copy(source, writer, "UTF8")
                        data = writer.toString()
                        data = data.replace("#DATA#", portfolio.chartData!!)
                        data = data.replace("#DRAW#", portfolio.chartDraw!!)
                        data = data.replace("#COLOR#", portfolio.chartColors!!)
                        data = data.replace("#DATE#", portfolio.chartDate!!)
                        data = data.replace("#WIDTH#".toRegex(), ((metrics.widthPixels / metrics.density).toInt() - 30).toString() + "")
                        data = data.replace("#HEIGHT#".toRegex(), (metrics.heightPixels.toDouble() / metrics.density.toDouble() / 1.35).toInt().toString() + "")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        actionBar.title = "Share Value Chart"
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, e.message, e)
            } finally {
                IOUtils.closeQuietly(source)
            }
            return data
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mainTask: MainTask
        when (item.itemId) {
            R.id.action_logout -> {
                mainTask = MainTask(this, UrlType.LOGOUT, emptyMap())
                mainTask.execute(null as Void?)
                return true
            }
            R.id.refresh -> {
                menuItem = item
                menuItem.setActionView(R.layout.progressbar)
                menuItem.expandActionView()
                mainTask = MainTask(this, UrlType.RELOAD, emptyMap())
                mainTask.execute(null as Void?)
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun reloadData(portfolio: Portfolio) {
        menuItem.collapseActionView()
        menuItem.actionView = null
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.PORTFOLIO, portfolio)
        this.portfolio = portfolio
        val data = data
        webView.loadDataWithBaseURL("file:///android_asset/www/", data, "text/html", "UTF-8", null)
        setResult(Activity.RESULT_OK, resultIntent)
        val app = application as StockTrackerApp
        app.toast()
    }

    override fun displayError(json: JSONObject) {
        val sessionError = (application as StockTrackerApp).isSessionError(json)
        if (sessionError) {
            (application as StockTrackerApp).loadErrorActivity(this, json)
        } else {
            errorView.text = json.optString("error")
            val params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            params.addRule(RelativeLayout.BELOW, errorView.id)
            val webView = findViewById<WebView>(R.id.webView)
            webView.layoutParams = params
            menuItem.collapseActionView()
            menuItem.actionView = null
        }
    }

    override fun logOut() {
        (application as StockTrackerApp).logOut(this)
    }

    companion object {
        private val TAG = "ChartActivity"
    }
}
