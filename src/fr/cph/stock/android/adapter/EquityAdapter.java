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

package fr.cph.stock.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.cph.stock.android.Constants;
import fr.cph.stock.android.R;
import fr.cph.stock.android.domain.Equity;

public class EquityAdapter extends BaseAdapter {

	private final List<Equity> equities;
	private final Context context;

	public EquityAdapter(final List<Equity> equities, final Context context) {
		this.equities = equities;
		this.context = context;
	}

	@Override
	public int getCount() {
		return equities.size();
	}

	@Override
	public Object getItem(final int position) {
		return equities.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final Equity equity = (Equity) getItem(position);
		final ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			final LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
			convertView = inflater.inflate(R.layout.list_item_equity, parent, false);

			viewHolder.name = convertView.findViewById(R.id.name);
			viewHolder.unitCostPrice = convertView.findViewById(R.id.unitCostPriceValue);
			viewHolder.value = convertView.findViewById(R.id.value);
			viewHolder.plusMinusValue = convertView.findViewById(R.id.plusMinusValue);
			viewHolder.quantity = convertView.findViewById(R.id.quantityValue);
			viewHolder.yieldYear = convertView.findViewById(R.id.yieldYear);
			viewHolder.quote = convertView.findViewById(R.id.quoteValue);
			viewHolder.gain = convertView.findViewById(R.id.gain);
			viewHolder.today = convertView.findViewById(R.id.today);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(equity.getName());
		viewHolder.unitCostPrice.setText(equity.getUnitCostPrice());
		viewHolder.value.setText(equity.getValue());
		viewHolder.plusMinusValue.setText(equity.getPlusMinusValue());
		viewHolder.plusMinusValue.setTextColor(equity.isUp() ? Constants.INSTANCE.getGREEN() : Constants.INSTANCE.getRED());
		viewHolder.quantity.setText(equity.getQuantity());
		viewHolder.yieldYear.setText(equity.getYieldUnitCostPrice());
		viewHolder.quote.setText(equity.getQuote());
		viewHolder.gain.setText(equity.getPlusMinusUnitCostPriceValue());
		viewHolder.today.setText(equity.getVariation());
		viewHolder.today.setTextColor(equity.isUpVariation() ? Constants.INSTANCE.getGREEN() : Constants.INSTANCE.getRED());

		return convertView;
	}

	private class ViewHolder {
		TextView name;
		TextView unitCostPrice;
		TextView value;
		TextView plusMinusValue;
		TextView quantity;
		TextView yieldYear;
		TextView quote;
		TextView gain;
		TextView today;
	}
}
