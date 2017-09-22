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

package fr.cph.stock.android.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import fr.cph.stock.android.R
import fr.cph.stock.android.domain.ShareValue

class ShareValueAdapter(private val sharesValues: List<ShareValue>, private val context: Context) : BaseAdapter() {

    override fun getCount(): Int {
        return sharesValues.size
    }

    override fun getItem(position: Int): Any {
        return sharesValues[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val shareValue = getItem(position) as ShareValue
        val viewHolder: ViewHolder

        if (convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context.applicationContext)
            convertView = inflater.inflate(R.layout.list_item_share_value, parent, false)

            viewHolder.dateView = convertView!!.findViewById(R.id.date)
            viewHolder.shareValueView = convertView.findViewById(R.id.shareValue)
            viewHolder.commentaryView = convertView.findViewById(R.id.commentary)
            viewHolder.accountView = convertView.findViewById(R.id.account)
            viewHolder.portfolioValueView = convertView.findViewById(R.id.portfolioValue)
            viewHolder.shareQuantityView = convertView.findViewById(R.id.shareQuantity)
            viewHolder.monthlyYieldView = convertView.findViewById(R.id.monthlyYield2)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.dateView!!.text = shareValue.date
        viewHolder.shareValueView!!.text = shareValue.shareValue
        viewHolder.shareValueView!!.setTextColor(Color.rgb(0, 160, 0))
        viewHolder.commentaryView!!.text = shareValue.commentary
        viewHolder.accountView!!.text = shareValue.account
        viewHolder.portfolioValueView!!.text = shareValue.portfolioValue
        viewHolder.shareQuantityView!!.text = shareValue.shareQuantity
        viewHolder.monthlyYieldView!!.text = shareValue.monthlyYield

        return convertView
    }

    private inner class ViewHolder {
        internal var dateView: TextView? = null
        internal var shareValueView: TextView? = null
        internal var commentaryView: TextView? = null
        internal var accountView: TextView? = null
        internal var portfolioValueView: TextView? = null
        internal var shareQuantityView: TextView? = null
        internal var monthlyYieldView: TextView? = null
    }

}
