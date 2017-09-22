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

package fr.cph.stock.android.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.cph.stock.android.util.UserContext;

import static fr.cph.stock.android.util.UserContext.FORMAT_CURRENCY_ZERO;
import static fr.cph.stock.android.util.UserContext.FORMAT_LOCAL_ONE;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Portfolio implements Parcelable {

	private Locale locale;
	private Double totalValue;
	private Double totalGain;
	private Double totalPlusMinusValue;
	private boolean up;
	private Double liquidity;
	private Double yieldYear;
	private String yieldYearPerc;
	private String lastUpdate;

	private Performance performance;

	@JsonProperty("chartShareValueColors")
	private String chartColors;
	@JsonProperty("chartShareValueData")
	private String chartData;
	@JsonProperty("chartShareValueDate")
	private String chartDate;
	@JsonProperty("chartShareValueDraw")
	private String chartDraw;

	private String chartSectorData;
	private String chartSectorTitle;
	private String chartSectorDraw;
	private String chartSectorCompanies;

	private String chartCapData;
	private String chartCapTitle;
	private String chartCapDraw;
	@JsonProperty("chartCapCompanies")
	private String chartCapCompanies;

	private Double totalVariation;

	private List<Equity> equities = new ArrayList<>();
	private List<ShareValue> shareValues;
	private List<Account> accounts;

	public Portfolio() {
	}

	public Portfolio(Parcel in) {
		readFromParcel(in);
	}

	public boolean isUp() {
		return totalGain > 0;
	}

	public String getTotalValue() {
		return UserContext.FORMAT_CURRENCY_ZERO.format(totalValue);
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public String getTotalGain() {
		return UserContext.FORMAT_CURRENCY_ZERO.format(totalGain);
	}

	public void setTotalGain(Double totalGain) {
		this.totalGain = totalGain;
	}

	public String getTotalPlusMinusValue() {
		return totalPlusMinusValue > 0
				? "+" + FORMAT_LOCAL_ONE.format(totalPlusMinusValue) + "%"
				: FORMAT_LOCAL_ONE.format(totalPlusMinusValue) + "%";
	}

	public void setTotalPlusMinusValue(final Double totalPlusMinusValue) {
		this.totalPlusMinusValue = totalPlusMinusValue;
	}

	public String getLiquidity() {
		return UserContext.FORMAT_CURRENCY_ZERO.format(liquidity);
	}

	public void setLiquidity(final Double liquidity) {
		this.liquidity = liquidity;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public List<Equity> getEquities() {
		return equities;
	}

	public void setEquities(List<Equity> equities) {
		this.equities = equities;
	}

	public String getYieldYear() {
		return FORMAT_CURRENCY_ZERO.format(yieldYear);
	}

	public void setYieldYear(Double yieldYear) {
		this.yieldYear = yieldYear;
	}

	public String getYieldYearPerc() {
		return yieldYearPerc;
	}

	public void setYieldYearPerc(String yieldYearPerc) {
		this.yieldYearPerc = yieldYearPerc;
	}

	public List<ShareValue> getShareValues() {
		return shareValues;
	}

	public void setShareValues(List<ShareValue> shareValues) {
		this.shareValues = shareValues;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public String getChartColors() {
		return chartColors;
	}

	public void setChartColors(String chartColors) {
		this.chartColors = chartColors;
	}

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	public String getChartDate() {
		return chartDate;
	}

	public void setChartDate(String chartDate) {
		this.chartDate = chartDate;
	}

	public String getChartDraw() {
		return chartDraw;
	}

	public void setChartDraw(String chartDraw) {
		this.chartDraw = chartDraw;
	}

	public String getChartSectorData() {
		return chartSectorData;
	}

	public void setChartSectorData(String chartSectorData) {
		this.chartSectorData = chartSectorData;
	}

	public String getChartSectorTitle() {
		return chartSectorTitle;
	}

	public void setChartSectorTitle(String chartSectorTitle) {
		this.chartSectorTitle = chartSectorTitle;
	}

	public String getChartSectorDraw() {
		return chartSectorDraw;
	}

	public void setChartSectorDraw(String chartSectorDraw) {
		this.chartSectorDraw = chartSectorDraw;
	}

	public String getChartSectorCompanies() {
		return chartSectorCompanies;
	}

	public void setChartSectorCompanies(String chartSectorCompanies) {
		this.chartSectorCompanies = chartSectorCompanies;
	}

	public String getChartCapData() {
		return chartCapData;
	}

	public void setChartCapData(String chartCapData) {
		this.chartCapData = chartCapData;
	}

	public String getChartCapTitle() {
		return chartCapTitle;
	}

	public void setChartCapTitle(String chartCapTitle) {
		this.chartCapTitle = chartCapTitle;
	}

	public String getChartCapDraw() {
		return chartCapDraw;
	}

	public void setChartCapDraw(String chartCapDraw) {
		this.chartCapDraw = chartCapDraw;
	}

	public String getChartCapCompanies() {
		return chartCapCompanies;
	}

	public void setChartCapCompanies(String chartCapCompanies) {
		this.chartCapCompanies = chartCapCompanies;
	}

	public String getTotalVariation() {
		final String plus = isTodayUp() ? "+" : "";
		return plus + UserContext.FORMAT_LOCAL_TWO.format(totalVariation) + "%";
	}

	public void setTotalVariation(final Double totalVariation) {
		this.totalVariation = totalVariation;
	}

	public boolean isTodayUp() {
		return totalVariation >= 0;
	}

	public Performance getPerformance() {
		return performance;
	}

	public void setPerformance(final Performance performance) {
		this.performance = performance;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(totalValue);
		dest.writeDouble(totalGain);
		dest.writeDouble(totalPlusMinusValue);
		dest.writeDouble(liquidity);
		dest.writeByte((byte) (up ? 1 : 0)); // myBoolean = in.readByte() == 1;
		dest.writeDouble(yieldYear);
		dest.writeString(yieldYearPerc);
		dest.writeString(lastUpdate);

		dest.writeParcelable(performance, flags);

		dest.writeString(chartColors);
		dest.writeString(chartData);
		dest.writeString(chartDate);
		dest.writeString(chartDraw);

		dest.writeString(chartSectorData);
		dest.writeString(chartSectorTitle);
		dest.writeString(chartSectorDraw);
		dest.writeString(chartSectorCompanies);

		dest.writeString(chartCapData);
		dest.writeString(chartCapTitle);
		dest.writeString(chartCapDraw);
		dest.writeString(chartCapCompanies);

		dest.writeTypedList(equities);
		dest.writeTypedList(shareValues);
		dest.writeTypedList(accounts);

		dest.writeDouble(totalVariation);
	}

	private void readFromParcel(Parcel in) {
		totalValue = in.readDouble();
		totalGain = in.readDouble();
		totalPlusMinusValue = in.readDouble();
		liquidity = in.readDouble();
		up = in.readByte() == 1;
		yieldYear = in.readDouble();
		yieldYearPerc = in.readString();
		lastUpdate = in.readString();
		performance = in.readParcelable(Performance.class.getClassLoader());
		chartColors = in.readString();
		chartData = in.readString();
		chartDate = in.readString();
		chartDraw = in.readString();
		chartSectorData = in.readString();
		chartSectorTitle = in.readString();
		chartSectorDraw = in.readString();
		chartSectorCompanies = in.readString();
		chartCapData = in.readString();
		chartCapTitle = in.readString();
		chartCapDraw = in.readString();
		chartCapCompanies = in.readString();
		equities = new ArrayList<>();
		in.readTypedList(equities, Equity.CREATOR);
		shareValues = new ArrayList<>();
		in.readTypedList(shareValues, ShareValue.CREATOR);
		accounts = new ArrayList<>();
		in.readTypedList(accounts, Account.CREATOR);
		totalVariation = in.readDouble();
	}

	public static final Parcelable.Creator<Portfolio> CREATOR = new Parcelable.Creator<Portfolio>() {
		public Portfolio createFromParcel(Parcel in) {
			return new Portfolio(in);
		}

		public Portfolio[] newArray(int size) {
			return new Portfolio[size];
		}
	};

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
