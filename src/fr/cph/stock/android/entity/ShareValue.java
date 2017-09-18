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

import fr.cph.stock.android.util.UserContext;

import static fr.cph.stock.android.util.UserContext.FORMAT_LOCAL_ONE;

public class ShareValue implements Parcelable {

	private String date;
	private String account;
	private Double portfolioValue;
	private Double shareQuantity;
	private Double shareValue;
	private Double monthlyYield;
	private boolean up;
	private String commentary;

	public ShareValue() {
	}

	public ShareValue(Parcel in) {
		readFromParcel(in);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getShareValue() {
		return FORMAT_LOCAL_ONE.format(shareValue);
	}

	public void setShareValue(final Double shareValue) {
		this.shareValue = shareValue;
	}

	public boolean isUp() {
		return shareValue > 100;
	}

	public String getPortfolioValue() {
		return UserContext.FORMAT_CURRENCY_ONE.format(portfolioValue);
	}

	public void setPortfolioValue(Double portfolioValue) {
		this.portfolioValue = portfolioValue;
	}

	public String getShareQuantity() {
		return UserContext.FORMAT_LOCAL_ONE.format(shareQuantity);
	}

	public void setShareQuantity(Double shareQuantity) {
		this.shareQuantity = shareQuantity;
	}

	public String getMonthlyYield() {
		return UserContext.FORMAT_CURRENCY_ONE.format(monthlyYield);
	}

	public void setMonthlyYield(Double monthlyYield) {
		this.monthlyYield = monthlyYield;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeString(account);
		dest.writeDouble(portfolioValue);
		dest.writeDouble(shareQuantity);
		dest.writeDouble(shareValue);
		dest.writeDouble(monthlyYield);
		dest.writeString(commentary);
		dest.writeByte((byte) (up ? 1 : 0)); // myBoolean = in.readByte() == 1;
	}

	private void readFromParcel(Parcel in) {
		date = in.readString();
		account = in.readString();
		portfolioValue = in.readDouble();
		shareQuantity = in.readDouble();
		shareValue = in.readDouble();
		monthlyYield = in.readDouble();
		commentary = in.readString();
		up = in.readByte() == 1;

	}

	public static final Parcelable.Creator<ShareValue> CREATOR = new Parcelable.Creator<ShareValue>() {
		public ShareValue createFromParcel(Parcel in) {
			return new ShareValue(in);
		}

		public ShareValue[] newArray(int size) {
			return new ShareValue[size];
		}
	};

}
