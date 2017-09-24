package fr.cph.stock.android.domain

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import fr.cph.stock.android.util.UserContext
import kotlin.properties.Delegates

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Performance constructor() : Parcelable {

    private var gain: Double by Delegates.notNull()
    private var performance: Double by Delegates.notNull()
    private var `yield`: Double by Delegates.notNull()
    private var taxes: Double by Delegates.notNull()

    fun getGain(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(gain)
    }

    fun getPerformance(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(performance) + "%"
    }

    fun getYield(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(`yield`)
    }

    fun getTaxes(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(taxes)
    }

    constructor(source: Parcel) : this() {
        gain = source.readDouble()
        performance = source.readDouble()
        `yield` = source.readDouble()
        taxes = source.readDouble()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(gain)
        dest.writeDouble(performance)
        dest.writeDouble(`yield`)
        dest.writeDouble(taxes)
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
