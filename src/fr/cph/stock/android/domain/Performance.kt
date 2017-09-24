package fr.cph.stock.android.domain

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import fr.cph.stock.android.util.UserContext

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Performance constructor() : Parcelable {

    private var gain: Double? = null
    private var performance: Double? = null
    @JsonProperty("yield")
    private var yield2: Double? = null
    private var taxes: Double? = null

    fun getGain(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(gain)
    }

    fun getPerformance(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(performance) + "%"
    }

    fun getYield(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(yield2)
    }

    fun getTaxes(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(taxes)
    }

    constructor(source: Parcel) : this() {
        if (source.readByte().toInt() == 0) {
            gain = null
        } else {
            gain = source.readDouble()
        }
        if (source.readByte().toInt() == 0) {
            performance = null
        } else {
            performance = source.readDouble()
        }
        if (source.readByte().toInt() == 0) {
            yield2 = null
        } else {
            yield2 = source.readDouble()
        }
        taxes = source.readDouble()
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
            override fun createFromParcel(source: Parcel): Performance {
                return Performance(source)
            }

            override fun newArray(size: Int): Array<Performance?> {
                return arrayOfNulls(size)
            }
        }
    }
}
