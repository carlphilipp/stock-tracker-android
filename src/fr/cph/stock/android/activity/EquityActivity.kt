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

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import fr.cph.stock.android.Constants
import fr.cph.stock.android.Constants.ERROR
import fr.cph.stock.android.R
import fr.cph.stock.android.StockTrackerApp
import fr.cph.stock.android.adapter.EquityAdapter
import fr.cph.stock.android.domain.Equity
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask
import org.json.JSONObject

class EquityActivity : ListActivity(), StockTrackerActivity {

    private lateinit var mAdapter: EquityAdapter
    private lateinit var equities: MutableList<Equity>
    private lateinit var lastUpdatedView: TextView
    private lateinit var errorView: TextView
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.equity_list_activity)
        errorView = findViewById(R.id.errorMessage)

        val portfolio = intent.getParcelableExtra<Portfolio>(Constants.PORTFOLIO)

        equities = portfolio.equities.toMutableList()
        mAdapter = EquityAdapter(equities, applicationContext)
        listAdapter = mAdapter

        lastUpdatedView = findViewById(R.id.lastUpdated)
        lastUpdatedView.text = portfolio.lastUpdate
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
        android.R.id.home -> consume { finish() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(portfolio: Portfolio) {
        equities.clear()
        equities.addAll(portfolio.equities)
        mAdapter.notifyDataSetChanged()
        lastUpdatedView.text = portfolio.lastUpdate
        menuItem.collapseActionView()
        menuItem.actionView = null
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.PORTFOLIO, portfolio)
        setResult(Activity.RESULT_OK, resultIntent)
        val app = application as StockTrackerApp
        app.toast()
    }

    override fun displayError(json: JSONObject) {
        val sessionError = (application as StockTrackerApp).isSessionError(json)
        if (sessionError) {
            (application as StockTrackerApp).loadErrorActivity(this, json)
        } else {
            errorView.text = json.optString(ERROR)
            val params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            params.addRule(RelativeLayout.BELOW, errorView.id)
            val listView = findViewById<ListView>(android.R.id.list)
            listView.layoutParams = params
            menuItem.collapseActionView()
            menuItem.actionView = null
        }
    }

    override fun logOut() {
        (application as StockTrackerApp).logOut(this)
    }
}
