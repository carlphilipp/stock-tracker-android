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

package fr.cph.stock.android.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import fr.cph.stock.android.Constants.GREEN
import fr.cph.stock.android.Constants.RED
import fr.cph.stock.android.R
import fr.cph.stock.android.domain.ChartType
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.listener.ChartListener

class MainListAdapter(private val activity: Activity, private var portfolio: Portfolio) : BaseAdapter() {

    override fun isEnabled(position: Int): Boolean {
        return position != 3
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        var textView: TextView
        when (position) {
            0 -> {
                if (convertView == null) {
                    val inflater = LayoutInflater.from(activity)
                    convertView = inflater.inflate(R.layout.main_list_item_cell1, parent, false)
                }
                textView = convertView!!.findViewById(R.id.portfolio_value_main)
                textView.text = portfolio.getTotalValue()
                textView = convertView.findViewById(R.id.liquidity_value_main)
                textView.text = portfolio.getLiquidity()
            }
            1 -> {
                if (convertView == null) {
                    val inflater = LayoutInflater.from(activity)
                    convertView = inflater.inflate(R.layout.main_list_item_cell2, parent, false)
                }
                textView = convertView!!.findViewById(R.id.current_performance_value)
                textView.text = portfolio.getTotalGain()
                textView.setTextColor(if (portfolio.isUp) GREEN else RED)

                textView = convertView.findViewById(R.id.today_performance_value)
                textView.text = portfolio.getTotalVariation()
                textView.setTextColor(if (portfolio.isTodayUp) GREEN else RED)
            }
            2 -> {
                if (convertView == null) {
                    val inflater = LayoutInflater.from(activity)
                    convertView = inflater.inflate(R.layout.main_list_item_cell3, parent, false)
                }
                textView = convertView!!.findViewById(R.id.performance_value)
                textView.text = portfolio.shareValues[0].getShareValue()
                textView.setTextColor(if (portfolio.shareValues[0].isUp) GREEN else RED)
                textView = convertView.findViewById(R.id.last_updated_value)
                textView.text = portfolio.lastUpdate
            }
            3 -> {
                if (convertView == null) {
                    val inflater = LayoutInflater.from(activity)
                    convertView = inflater.inflate(R.layout.main_list_item_cell4, parent, false)
                }
                val shareValueView = convertView!!.findViewById<ImageButton>(R.id.shareValueChart)
                val chartShareValueListener = ChartListener(activity, portfolio, ChartType.SHARE_VALUE)
                shareValueView.setOnClickListener(chartShareValueListener)

                val sectorChartView = convertView.findViewById<ImageView>(R.id.sectorChart)
                val chartSectorListener = ChartListener(activity, portfolio, ChartType.SECTOR)
                sectorChartView.setOnClickListener(chartSectorListener)

                val capChartView = convertView.findViewById<ImageView>(R.id.capChart)
                val chartCapListener = ChartListener(activity, portfolio, ChartType.CAPITALIZATION)
                capChartView.setOnClickListener(chartCapListener)
            }
        }
        return convertView!!
    }

    fun update(portfolio: Portfolio) {
        this.portfolio = portfolio
        this.notifyDataSetChanged()
    }
}
