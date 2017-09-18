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

package fr.cph.stock.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import fr.cph.stock.android.util.UserContext;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Equity implements Parcelable {

	private String name;
	private Double unitCostPrice;
	private Double value;
	private Double plusMinusValue;
	private boolean up;
	private boolean upVariation;
	private Double quantity;
	private Double yieldYear;
	private Double yieldUnitCostPrice;
	private Double quote;
	private Double plusMinusUnitCostPriceValue;
	private String variation;

	public Equity() {

	}

	public Equity(Parcel in) {
		readFromParcel(in);
	}

	public String getQuantity() {
		return UserContext.FORMAT_LOCAL_ONE.format(quantity);
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getYieldYear() {
		return UserContext.FORMAT_LOCAL_ONE.format(yieldYear) + "%";
	}

	public void setYieldYear(Double yieldYear) {
		this.yieldYear = yieldYear;
	}

	public String getYieldUnitCostPrice() {
		return UserContext.FORMAT_LOCAL_ONE.format(yieldUnitCostPrice) + "%";
	}

	public void setYieldUnitCostPrice(Double yieldUnitCostPrice) {
		this.yieldUnitCostPrice = yieldUnitCostPrice;
	}

	public String getQuote() {
		return UserContext.FORMAT_LOCAL_TWO.format(quote);
	}

	public void setQuote(Double quote) {
		this.quote = quote;
	}

	public String getPlusMinusUnitCostPriceValue() {
		return plusMinusValue > 0
				? "+" + UserContext.FORMAT_LOCAL_ZERO.format(plusMinusUnitCostPriceValue)
				: UserContext.FORMAT_LOCAL_ZERO.format(plusMinusUnitCostPriceValue);
	}

	public void setPlusMinusUnitCostPriceValue(final Double plusMinusUnitCostPriceValue) {
		this.plusMinusUnitCostPriceValue = plusMinusUnitCostPriceValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitCostPrice() {
		return UserContext.FORMAT_LOCAL_TWO.format(unitCostPrice);
	}

	public void setUnitCostPrice(Double unitCostPrice) {
		this.unitCostPrice = unitCostPrice;
	}

	public String getValue() {
		return UserContext.FORMAT_LOCAL_ZERO.format(value);
	}

	public void setValue(final Double value) {
		this.value = value;
	}

	public String getPlusMinusValue() {
		return plusMinusValue > 0
				? "+" + UserContext.FORMAT_LOCAL_ONE.format(plusMinusValue) + "%"
				: UserContext.FORMAT_LOCAL_ONE.format(plusMinusValue) + "%";
	}

	public void setPlusMinusValue(Double plusMinusValue) {
		this.plusMinusValue = plusMinusValue;
	}

	public boolean isUp() {
		return plusMinusValue > 0;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getVariation() {
		return variation == null ? "?" : variation;
	}

	public void setVariation(String variation) {
		this.variation = variation;
	}

	public boolean isUpVariation() {
		return upVariation;
	}

	public void setUpVariation(boolean upVariation) {
		this.upVariation = upVariation;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeDouble(unitCostPrice);
		dest.writeDouble(value);
		dest.writeDouble(plusMinusValue);
		dest.writeDouble(quantity);
		dest.writeDouble(yieldYear);
		dest.writeDouble(yieldUnitCostPrice);
		dest.writeDouble(quote);
		dest.writeDouble(plusMinusUnitCostPriceValue);
		dest.writeByte((byte) (up ? 1 : 0)); // myBoolean = in.readByte() == 1;
		dest.writeString(variation);
		dest.writeByte((byte) (upVariation ? 1 : 0));
	}

	private void readFromParcel(Parcel in) {
		name = in.readString();
		unitCostPrice = in.readDouble();
		value = in.readDouble();
		plusMinusValue = in.readDouble();
		quantity = in.readDouble();
		yieldYear = in.readDouble();
		yieldUnitCostPrice = in.readDouble();
		quote = in.readDouble();
		plusMinusUnitCostPriceValue = in.readDouble();
		up = in.readByte() == 1;
		variation = in.readString();
		upVariation = in.readByte() == 1;
	}

	public static final Parcelable.Creator<Equity> CREATOR = new Parcelable.Creator<Equity>() {
		public Equity createFromParcel(Parcel in) {
			return new Equity(in);
		}

		public Equity[] newArray(int size) {
			return new Equity[size];
		}
	};

}
