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
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import fr.cph.stock.android.Constants
import fr.cph.stock.android.Constants.GREEN
import fr.cph.stock.android.Constants.RED
import fr.cph.stock.android.R
import fr.cph.stock.android.StockTrackerApp
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask
import fr.cph.stock.android.util.Util
import org.json.JSONObject

/**
 * This class represents the account activity
 *
 * @author Carl-Philipp Harmant
 */
class AccountActivity : Activity(), StockTrackerActivity {

    private lateinit var portfolio: Portfolio
    private lateinit var menuItem: MenuItem
    private lateinit var errorView: TextView
    private lateinit var totalValueView: TextView
    private lateinit var totalGainView: TextView
    private lateinit var totalPlusMinusValueView: TextView
    private lateinit var lastUpdateView: TextView
    private lateinit var liquidityView: TextView
    private lateinit var yieldYearView: TextView
    private lateinit var shareValueView: TextView
    private lateinit var gainView: TextView
    private lateinit var perfView: TextView
    private lateinit var yieldView: TextView
    private lateinit var taxesView: TextView
    private val textViews: MutableList<TextView> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "Account Activity onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)

        portfolio = intent.getParcelableExtra(Constants.PORTFOLIO)
        errorView = findViewById(R.id.errorMessage)
        totalValueView = findViewById(R.id.totalValue)
        totalGainView = findViewById(R.id.totalGain)
        totalPlusMinusValueView = findViewById(R.id.totalPlusMinusValue)
        lastUpdateView = findViewById(R.id.lastUpdate)
        liquidityView = findViewById(R.id.liquidity)
        yieldYearView = findViewById(R.id.yieldYear)
        shareValueView = findViewById(R.id.shareValue)
        gainView = findViewById(R.id.gain2)
        perfView = findViewById(R.id.perf)
        yieldView = findViewById(R.id.yieldPerf)
        taxesView = findViewById(R.id.taxes)

        val accountsLayout = findViewById<RelativeLayout>(R.id.accountsLayout)
        var recent = TextView(applicationContext)
        var id = 1
        var nameID = 100
        var viewId1 = 500
        var currencyId = 1000
        for (i in 0 until portfolio.accounts.size) {
            val account = portfolio.accounts[i]
            val currentAccountNameTextView = TextView(applicationContext)
            currentAccountNameTextView.text = account.name
            currentAccountNameTextView.setTextColor(Color.GRAY)
            currentAccountNameTextView.id = nameID
            var params = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            if (i != 0) {
                params.addRule(RelativeLayout.BELOW, recent.id)
            }
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            currentAccountNameTextView.layoutParams = params
            accountsLayout.addView(currentAccountNameTextView, params)
            textViews.add(currentAccountNameTextView)

            val viewPoint1 = View(applicationContext)
            viewPoint1.id = viewId1
            viewPoint1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_light))
            params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2)
            params.addRule(RelativeLayout.RIGHT_OF, nameID)
            params.addRule(RelativeLayout.LEFT_OF, currencyId)
            params.setMargins(0, Util.convertDpToPxl(15, applicationContext), 0, 0)
            if (i != 0) {
                params.addRule(RelativeLayout.BELOW, recent.id)
            }
            viewPoint1.layoutParams = params
            accountsLayout.addView(viewPoint1, params)

            val currentCurrencyTextView = TextView(applicationContext)
            currentCurrencyTextView.text = account.currency
            currentCurrencyTextView.setTextColor(Color.GRAY)
            currentCurrencyTextView.id = currencyId
            params = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            if (i != 0) {
                params.addRule(RelativeLayout.BELOW, recent.id)
            }
            currentCurrencyTextView.layoutParams = params
            accountsLayout.addView(currentCurrencyTextView, params)
            textViews.add(currentCurrencyTextView)

            val viewPoint2 = View(applicationContext)
            viewPoint2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_light))
            params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2)
            params.addRule(RelativeLayout.RIGHT_OF, currencyId)
            params.addRule(RelativeLayout.LEFT_OF, id)
            params.setMargins(0, Util.convertDpToPxl(15, applicationContext), 0, 0)
            if (i != 0) {
                params.addRule(RelativeLayout.BELOW, recent.id)
            }
            viewPoint2.layoutParams = params
            accountsLayout.addView(viewPoint2, params)

            val currentTextView = TextView(applicationContext)
            currentTextView.text = account.getLiquidity()
            currentTextView.setTextColor(Color.GRAY)
            currentTextView.id = id
            params = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            if (i != 0) {
                params.addRule(RelativeLayout.BELOW, recent.id)
            }
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            currentTextView.layoutParams = params
            recent = currentTextView
            accountsLayout.addView(currentTextView, params)
            textViews.add(currentTextView)

            id++
            nameID++
            viewId1++
            currencyId++
        }
        buildUi(false)
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
        menuItem.collapseActionView()
        menuItem.actionView = null
        this.portfolio = portfolio
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.PORTFOLIO, portfolio)
        setResult(Activity.RESULT_OK, resultIntent)
        buildUi(true)
        val app = application as StockTrackerApp
        app.toast()
    }

    override fun displayError(json: JSONObject) {
        val sessionError = (application as StockTrackerApp).isSessionError(json)
        if (sessionError) {
            (application as StockTrackerApp).loadErrorActivity(this, json)
        } else {
            errorView.text = json.optString("error")
            val params = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.addRule(RelativeLayout.BELOW, errorView.id)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            totalValueView.layoutParams = params
            menuItem.collapseActionView()
            menuItem.actionView = null
        }
    }

    override fun logOut() {
        (application as StockTrackerApp).logOut(this)
    }

    private fun buildUi(withAccounts: Boolean) {
        totalValueView.text = portfolio.getTotalValue()
        totalGainView.text = portfolio.getTotalGain()
        totalGainView.setTextColor(if (portfolio.isUp) GREEN else RED)
        val totalPlusMinusValue = " (" + portfolio.getTotalPlusMinusValue() + ")"
        totalPlusMinusValueView.text = totalPlusMinusValue
        totalPlusMinusValueView.setTextColor(if (portfolio.isUp) GREEN else RED)
        lastUpdateView.text = portfolio.lastUpdate
        liquidityView.text = portfolio.getLiquidity()
        yieldYearView.text = portfolio.getYieldYear()
        shareValueView.text = portfolio.shareValues[0].getShareValue()
        gainView.text = portfolio.performance.getGain()
        perfView.text = portfolio.performance.getPerformance()
        yieldView.text = portfolio.performance.getYield()
        taxesView.text = portfolio.performance.getTaxes()
        if (withAccounts) {
            var j = 0
            val size = portfolio.accounts.size
            for (i in 0 until size) {
                val account = portfolio.accounts[i]

                val currentAccountNameTextView = textViews[j++]
                currentAccountNameTextView.text = account.name

                val currentTextView = textViews[j++]
                currentTextView.text = account.getLiquidity()

                val currentCurrencyTextView = textViews[j++]
                currentCurrencyTextView.text = account.currency
            }
        }
    }

    companion object {
        private val TAG = "AccountActivity"
    }
}
