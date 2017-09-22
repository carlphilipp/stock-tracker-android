package fr.cph.stock.android.domain

import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

import fr.cph.stock.android.util.UserContext

import fr.cph.stock.android.util.UserContext.FORMAT_CURRENCY_ONE
import fr.cph.stock.android.util.UserContext.FORMAT_LOCAL_ONE

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Performance : Parcelable {
    private var gain: Double? = null
    private var performance: Double? = null
    private var yield2: Double? = null
    private var taxes: Double? = null

    constructor() {}

    fun getGain(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(gain)
    }

    fun setGain(gain: Double?) {
        this.gain = gain
    }

    fun getPerformance(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(performance) + "%"
    }

    fun setPerformance(performance: Double?) {
        this.performance = performance
    }

    fun getYield(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(yield2)
    }

    fun setYield(yield2: Double?) {
        this.yield2 = yield2
    }

    fun getTaxes(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(taxes)
    }

    fun setTaxes(taxes: Double) {
        this.taxes = taxes
    }

    protected constructor(`in`: Parcel) {
        if (`in`.readByte().toInt() == 0) {
            gain = null
        } else {
            gain = `in`.readDouble()
        }
        if (`in`.readByte().toInt() == 0) {
            performance = null
        } else {
            performance = `in`.readDouble()
        }
        if (`in`.readByte().toInt() == 0) {
            yield2 = null
        } else {
            yield2 = `in`.readDouble()
        }
        taxes = `in`.readDouble()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (gain == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeDouble(gain!!)
        }
        if (performance == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeDouble(performance!!)
        }
        if (yield2 == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeDouble(yield2!!)
        }
        dest.writeDouble(taxes!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Performance> = object : Parcelable.Creator<Performance> {
            override fun createFromParcel(`in`: Parcel): Performance {
                return Performance(`in`)
            }

            override fun newArray(size: Int): Array<Performance?> {
                return arrayOfNulls(size)
            }
        }
    }
}
