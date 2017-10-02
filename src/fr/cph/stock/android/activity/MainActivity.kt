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

import android.app.ActionBar
import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.StockTrackerApp
import fr.cph.stock.android.adapter.MainListAdapter
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.listener.ErrorMainOnClickListener
import fr.cph.stock.android.task.MainTask
import org.json.JSONObject

class MainActivity : Activity(), StockTrackerActivity {

    private lateinit var menuItem: MenuItem
    private lateinit var ada: MainListAdapter
    private lateinit var portfolio: Portfolio
    private lateinit var errorView: TextView
    private lateinit var listView: ListView

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.main)

        portfolio = if (bundle != null)
            bundle.getParcelable(Constants.PORTFOLIO)
        else
            intent.getParcelableExtra(Constants.PORTFOLIO)

        ada = MainListAdapter(this, portfolio)
        listView = findViewById(R.id.mainList)
        listView.adapter = ada
        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    val intent = Intent(baseContext, AccountActivity::class.java)
                    intent.putExtra(Constants.PORTFOLIO, portfolio)
                    startActivityForResult(intent, MainActivity.ACCOUNT_REQUEST)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                1 -> {
                    val intent = Intent(baseContext, EquityActivity::class.java)
                    intent.putExtra(Constants.PORTFOLIO, portfolio)
                    startActivityForResult(intent, MainActivity.EQUITY_REQUEST)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                2 -> {
                    val intent = Intent(baseContext, OverallActivity::class.java)
                    intent.putExtra(Constants.PORTFOLIO, portfolio)
                    startActivityForResult(intent, MainActivity.OVERALL_REQUEST)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }

        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_SHOW_TITLE or ActionBar.DISPLAY_SHOW_CUSTOM

        errorView = findViewById(R.id.errorMessage)
        errorView.setOnClickListener(ErrorMainOnClickListener(listView, errorView))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        when (resultCode) {
            100 -> finish()
            Activity.RESULT_OK -> {
                portfolio = intent!!.getParcelableExtra(Constants.PORTFOLIO)
                ada.update(portfolio)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> consume { MainTask(this, UrlType.LOGOUT, emptyMap()).execute() }
        R.id.refresh -> consume {
            menuItem = item
            menuItem.setActionView(R.layout.progressbar)
            menuItem.expandActionView()
            MainTask(this, UrlType.RELOAD, emptyMap()).execute()
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(portfolio: Portfolio) {
        this.portfolio = portfolio
        menuItem.collapseActionView()
        menuItem.actionView = null
        ada.update(this.portfolio)
        val app = application as StockTrackerApp
        app.toast()
    }

    override fun displayError(json: JSONObject) {
        val sessionError = (application as StockTrackerApp).isSessionError(json)
        if (sessionError) {
            (application as StockTrackerApp).loadErrorActivity(this, json)
        } else {
            val params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            params.addRule(RelativeLayout.BELOW, errorView.id)
            listView.layoutParams = params
            errorView.text = json.optString("error")
            menuItem.collapseActionView()
            menuItem.actionView = null
        }
    }

    override fun logOut() {
        (application as StockTrackerApp).logOut(this)
    }

    companion object {
        val ACCOUNT_REQUEST = 1
        val EQUITY_REQUEST = 2
        val OVERALL_REQUEST = 3
        val CHART_REQUEST = 4
    }
}
