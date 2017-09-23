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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import fr.cph.stock.android.Constants
import fr.cph.stock.android.R
import fr.cph.stock.android.domain.Equity

class EquityAdapter(private val equities: List<Equity>, private val context: Context) : BaseAdapter() {

    override fun getCount(): Int {
        return equities.size
    }

    override fun getItem(position: Int): Any {
        return equities[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val equity = getItem(position) as Equity
        val viewHolder: ViewHolder

        if (convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context.applicationContext)
            convertView = inflater.inflate(R.layout.list_item_equity, parent, false)

            viewHolder.name = convertView!!.findViewById(R.id.name)
            viewHolder.unitCostPrice = convertView.findViewById(R.id.unitCostPriceValue)
            viewHolder.value = convertView.findViewById(R.id.value)
            viewHolder.plusMinusValue = convertView.findViewById(R.id.plusMinusValue)
            viewHolder.quantity = convertView.findViewById(R.id.quantityValue)
            viewHolder.yieldYear = convertView.findViewById(R.id.yieldYear)
            viewHolder.quote = convertView.findViewById(R.id.quoteValue)
            viewHolder.gain = convertView.findViewById(R.id.gain)
            viewHolder.today = convertView.findViewById(R.id.today)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.name!!.text = equity.name
        viewHolder.unitCostPrice!!.text = equity.getUnitCostPrice()
        viewHolder.value!!.text = equity.getValue()
        viewHolder.plusMinusValue!!.text = equity.getPlusMinusValue()
        viewHolder.plusMinusValue!!.setTextColor(if (equity.isUp) Constants.GREEN else Constants.RED)
        viewHolder.quantity!!.text = equity.getQuantity()
        viewHolder.yieldYear!!.text = equity.getYieldUnitCostPrice()
        viewHolder.quote!!.text = equity.getQuote()
        viewHolder.gain!!.text = equity.getPlusMinusUnitCostPriceValue()
        viewHolder.today!!.text = equity.getVariation()
        viewHolder.today!!.setTextColor(if (equity.isUpVariation) Constants.GREEN else Constants.RED)

        return convertView
    }

    private inner class ViewHolder {
        internal var name: TextView? = null
        internal var unitCostPrice: TextView? = null
        internal var value: TextView? = null
        internal var plusMinusValue: TextView? = null
        internal var quantity: TextView? = null
        internal var yieldYear: TextView? = null
        internal var quote: TextView? = null
        internal var gain: TextView? = null
        internal var today: TextView? = null
    }
}
