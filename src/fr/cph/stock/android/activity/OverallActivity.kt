/**
 * Copyright 2013 Carl-Philipp Harmant
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
import android.app.Dialog
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView

import org.json.JSONObject

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap

import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.StockTrackerApp
import fr.cph.stock.android.adapter.ShareValueAdapter
import fr.cph.stock.android.domain.Account
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.ShareValue
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask

class OverallActivity : ListActivity(), IStockTrackerActivity {

    private var refreshItem: MenuItem? = null
    private var shareValues: MutableList<ShareValue>? = null
    private var ada: ShareValueAdapter? = null
    private var errorView: TextView? = null
    private var portfolio: Portfolio? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.overall_activity)
        portfolio = if (bundle != null)
            bundle.getParcelable(Constants.PORTFOLIO)
        else
            intent.getParcelableExtra(Constants.PORTFOLIO)
        errorView = findViewById(R.id.errorMessage)
        shareValues = portfolio!!.shareValues!!.toMutableList()
        ada = ShareValueAdapter(shareValues!!, applicationContext)
        listAdapter = ada
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.overall, menu)
        refreshItem = menu.getItem(2)
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
                item.setActionView(R.layout.progressbar)
                item.expandActionView()
                mainTask = MainTask(this, UrlType.RELOAD, emptyMap())
                mainTask.execute(null as Void?)
                return true
            }
            R.id.action_update -> {
                showPanelUpdateHistory()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showPanelUpdateHistory() {
        val alert = Dialog(this)
        alert.setTitle("Update history")
        alert.setContentView(R.layout.history_dialog)

        val checked = alert.findViewById<Spinner>(R.id.accountList)
        val list = ArrayList<String>()
        for (acc in portfolio!!.accounts!!) {
            list.add(acc.name!!)
        }
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        checked.adapter = dataAdapter

        var dialogButton = alert.findViewById<Button>(R.id.dialogButtonOK)
        dialogButton.setOnClickListener { v ->
            refreshItem!!.setActionView(R.layout.progressbar)
            refreshItem!!.expandActionView()
            val account = portfolio!!.accounts!![checked.selectedItemPosition]
            val liquidityView = alert.findViewById<EditText>(R.id.liquidityMov)
            val yieldView = alert.findViewById<EditText>(R.id.yield)
            val buyView = alert.findViewById<EditText>(R.id.buy)
            val sellView = alert.findViewById<EditText>(R.id.sell)
            val taxeView = alert.findViewById<EditText>(R.id.taxe)
            val commentaryView = alert.findViewById<EditText>(R.id.commentaryEditText)

            val params = object : HashMap<String, String>() {
                init {
                    put("accountId", account.id!!)
                    put("liquidity", liquidityView.text.toString())
                    put("yield", yieldView.text.toString())
                    put("buy", buyView.text.toString())
                    put("sell", sellView.text.toString())
                    put("taxe", taxeView.text.toString())
                    put("commentary", commentaryView.text.toString().replace(" ".toRegex(), "%20"))
                }
            }
            val mainTask = MainTask(this@OverallActivity, UrlType.UPDATEHISTORY, params)
            mainTask.execute(null as Void?)
            alert.dismiss()
        }
        dialogButton = alert.findViewById(R.id.dialogButtonCancel)
        dialogButton.setOnClickListener { v -> alert.dismiss() }
        alert.show()
    }

    override fun reloadData(portfolio: Portfolio) {
        shareValues!!.clear()
        shareValues!!.addAll(portfolio.shareValues!!)
        ada!!.notifyDataSetChanged()
        refreshItem!!.collapseActionView()
        refreshItem!!.actionView = null
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.PORTFOLIO, portfolio)
        setResult(Activity.RESULT_OK, resultIntent)
        val app = application as StockTrackerApp
        app.toast()
    }

    override fun displayError(json: JSONObject) {
        Log.d(TAG, json.toString())
        val sessionError = (application as StockTrackerApp).isSessionError(json)
        if (sessionError) {
            (application as StockTrackerApp).loadErrorActivity(this, json)
        } else {
            errorView!!.text = json.optString("error")
            val params = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            params.addRule(RelativeLayout.BELOW, errorView!!.id)
            val listView = findViewById<ListView>(android.R.id.list)
            listView.layoutParams = params
            refreshItem!!.collapseActionView()
            refreshItem!!.actionView = null
        }
    }

    override fun logOut() {
        (application as StockTrackerApp).logOut(this)
    }

    companion object {

        private val TAG = "OverallActivity"
    }
}
