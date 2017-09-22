package fr.cph.stock.android.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import fr.cph.stock.android.util.UserContext;

import static fr.cph.stock.android.util.UserContext.FORMAT_CURRENCY_ONE;
import static fr.cph.stock.android.util.UserContext.FORMAT_LOCAL_ONE;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Performance implements Parcelable {
	private Double gain;
	private Double performance;
	private Double yield;
	private Double taxes;

	public Performance() {
	}

	public String getGain() {
		return UserContext.INSTANCE.getFORMAT_CURRENCY_ONE().format(gain);
	}

	public void setGain(Double gain) {
		this.gain = gain;
	}

	public String getPerformance() {
		return UserContext.INSTANCE.getFORMAT_LOCAL_ONE().format(performance) + "%";
	}

	public void setPerformance(Double performance) {
		this.performance = performance;
	}

	public String getYield() {
		return UserContext.INSTANCE.getFORMAT_LOCAL_ONE().format(yield);
	}

	public void setYield(Double yield) {
		this.yield = yield;
	}

	public String getTaxes() {
		return UserContext.INSTANCE.getFORMAT_CURRENCY_ONE().format(taxes);
	}

	public void setTaxes(double taxes) {
		this.taxes = taxes;
	}

	protected Performance(Parcel in) {
		if (in.readByte() == 0) {
			gain = null;
		} else {
			gain = in.readDouble();
		}
		if (in.readByte() == 0) {
			performance = null;
		} else {
			performance = in.readDouble();
		}
		if (in.readByte() == 0) {
			yield = null;
		} else {
			yield = in.readDouble();
		}
		taxes = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (gain == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeDouble(gain);
		}
		if (performance == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeDouble(performance);
		}
		if (yield == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeDouble(yield);
		}
		dest.writeDouble(taxes);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Performance> CREATOR = new Creator<Performance>() {
		@Override
		public Performance createFromParcel(Parcel in) {
			return new Performance(in);
		}

		@Override
		public Performance[] newArray(int size) {
			return new Performance[size];
		}
	};
}
