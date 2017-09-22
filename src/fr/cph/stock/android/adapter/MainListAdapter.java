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

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.cph.stock.android.R;
import fr.cph.stock.android.domain.Portfolio;
import fr.cph.stock.android.domain.ChartType;
import fr.cph.stock.android.listener.ChartListener;

import static fr.cph.stock.android.Constants.GREEN;
import static fr.cph.stock.android.Constants.RED;

public class MainListAdapter extends BaseAdapter {

	private final Activity activity;
	private Portfolio portfolio;

	public MainListAdapter(final Activity activity, final Portfolio portfolio) {
		this.activity = activity;
		this.portfolio = portfolio;
	}

	@Override
	public boolean isEnabled(final int position) {
		return position != 3;
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public Object getItem(final int position) {
		return null;
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		TextView textView;
		switch (position) {
			case 0:
				if (convertView == null) {
					final LayoutInflater inflater = LayoutInflater.from(activity);
					convertView = inflater.inflate(R.layout.main_list_item_cell1, parent, false);
				}
				textView = convertView.findViewById(R.id.portfolio_value_main);
				textView.setText(portfolio.getTotalValue());
				textView = convertView.findViewById(R.id.liquidity_value_main);
				textView.setText(portfolio.getLiquidity());

				break;
			case 1:
				if (convertView == null) {
					final LayoutInflater inflater = LayoutInflater.from(activity);
					convertView = inflater.inflate(R.layout.main_list_item_cell2, parent, false);
				}
				textView = convertView.findViewById(R.id.current_performance_value);
				textView.setText(portfolio.getTotalGain());
				textView.setTextColor(portfolio.isUp() ? GREEN : RED);

				textView = convertView.findViewById(R.id.today_performance_value);
				textView.setText(portfolio.getTotalVariation());
				textView.setTextColor(portfolio.isTodayUp() ? GREEN : RED);
				break;
			case 2:
				if (convertView == null) {
					final LayoutInflater inflater = LayoutInflater.from(activity);
					convertView = inflater.inflate(R.layout.main_list_item_cell3, parent, false);
				}
				textView = convertView.findViewById(R.id.performance_value);
				textView.setText(portfolio.getShareValues().get(0).getShareValue());
				textView.setTextColor(portfolio.getShareValues().get(0).isUp() ? GREEN : RED);
				textView = convertView.findViewById(R.id.last_updated_value);
				textView.setText(portfolio.getLastUpdate());
				break;
			case 3:
				if (convertView == null) {
					final LayoutInflater inflater = LayoutInflater.from(activity);
					convertView = inflater.inflate(R.layout.main_list_item_cell4, parent, false);
				}
				final ImageButton shareValueView = convertView.findViewById(R.id.shareValueChart);
				final ChartListener chartShareValueListener = new ChartListener(activity, portfolio, ChartType.SHARE_VALUE);
				shareValueView.setOnClickListener(chartShareValueListener);

				final ImageView sectorChartView = convertView.findViewById(R.id.sectorChart);
				final ChartListener chartSectorListener = new ChartListener(activity, portfolio, ChartType.SECTOR);
				sectorChartView.setOnClickListener(chartSectorListener);

				final ImageView capChartView = convertView.findViewById(R.id.capChart);
				final ChartListener chartCapListener = new ChartListener(activity, portfolio, ChartType.CAPITALIZATION);
				capChartView.setOnClickListener(chartCapListener);
				break;
		}
		return convertView;
	}

	public void update(final Portfolio portfolio) {
		this.portfolio = portfolio;
		this.notifyDataSetChanged();
	}
}
