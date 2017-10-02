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
import fr.cph.stock.android.Constants.GREEN
import fr.cph.stock.android.Constants.RED
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
        var view = convertView
        val equity = getItem(position) as Equity
        val viewHolder: ViewHolder

        if (view == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context.applicationContext)
            view = inflater.inflate(R.layout.list_item_equity, parent, false)

            viewHolder.name = view!!.findViewById(R.id.name)
            viewHolder.unitCostPrice = view.findViewById(R.id.unitCostPriceValue)
            viewHolder.value = view.findViewById(R.id.value)
            viewHolder.plusMinusValue = view.findViewById(R.id.plusMinusValue)
            viewHolder.quantity = view.findViewById(R.id.quantityValue)
            viewHolder.yieldYear = view.findViewById(R.id.yieldYear)
            viewHolder.quote = view.findViewById(R.id.quoteValue)
            viewHolder.gain = view.findViewById(R.id.gain)
            viewHolder.today = view.findViewById(R.id.today)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.name.text = equity.name
        viewHolder.unitCostPrice.text = equity.getUnitCostPrice()
        viewHolder.value.text = equity.getValue()
        viewHolder.plusMinusValue.text = equity.getPlusMinusValue()
        viewHolder.plusMinusValue.setTextColor(if (equity.isUp) GREEN else RED)
        viewHolder.quantity.text = equity.getQuantity()
        viewHolder.yieldYear.text = equity.getYieldUnitCostPrice()
        viewHolder.quote.text = equity.getQuote()
        viewHolder.gain.text = equity.getPlusMinusUnitCostPriceValue()
        viewHolder.today.text = equity.getVariation()
        viewHolder.today.setTextColor(if (equity.isUpVariation) GREEN else RED)

        return view
    }

    private inner class ViewHolder {
        lateinit var name: TextView
        lateinit var unitCostPrice: TextView
        lateinit var value: TextView
        lateinit var plusMinusValue: TextView
        lateinit var quantity: TextView
        lateinit var yieldYear: TextView
        lateinit var quote: TextView
        lateinit var gain: TextView
        lateinit var today: TextView
    }
}
