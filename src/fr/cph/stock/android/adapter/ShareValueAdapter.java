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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.cph.stock.android.R;
import fr.cph.stock.android.entity.ShareValue;

public class ShareValueAdapter extends BaseAdapter {

	private List<ShareValue> sharesValues;
	private Context context;

	public ShareValueAdapter(final List<ShareValue> sharesValues, final Context context) {
		this.sharesValues = sharesValues;
		this.context = context;
	}

	@Override
	public int getCount() {
		return sharesValues.size();
	}

	@Override
	public Object getItem(final int position) {
		return sharesValues.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ShareValue shareValue = (ShareValue) getItem(position);
		final ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			final LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
			convertView = inflater.inflate(R.layout.list_item_share_value, parent, false);

			viewHolder.dateView = convertView.findViewById(R.id.date);
			viewHolder.shareValueView = convertView.findViewById(R.id.shareValue);
			viewHolder.commentaryView = convertView.findViewById(R.id.commentary);
			viewHolder.accountView = convertView.findViewById(R.id.account);
			viewHolder.portfolioValueView = convertView.findViewById(R.id.portfolioValue);
			viewHolder.shareQuantityView = convertView.findViewById(R.id.shareQuantity);
			viewHolder.monthlyYieldView = convertView.findViewById(R.id.monthlyYield2);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.dateView.setText(shareValue.getDate());
		viewHolder.shareValueView.setText(shareValue.getShareValue());
		viewHolder.shareValueView.setTextColor(Color.rgb(0, 160, 0));
		viewHolder.commentaryView.setText(shareValue.getCommentary());
		viewHolder.accountView.setText(shareValue.getAccount());
		viewHolder.portfolioValueView.setText(shareValue.getPortfolioValue());
		viewHolder.shareQuantityView.setText(shareValue.getShareQuantity());
		viewHolder.monthlyYieldView.setText(shareValue.getMonthlyYield());

		return convertView;
	}

	private class ViewHolder {
		TextView dateView;
		TextView shareValueView;
		TextView commentaryView;
		TextView accountView;
		TextView portfolioValueView;
		TextView shareQuantityView;
		TextView monthlyYieldView;
	}

}
