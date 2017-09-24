/**
 * Copyright 2017 Carl-Philipp Harmant
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.cph.stock.android.domain

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import fr.cph.stock.android.util.UserContext
import java.util.*
import kotlin.properties.Delegates

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Portfolio constructor() : Parcelable {

    lateinit var locale: Locale
    private var totalValue: Double by Delegates.notNull()
    private var totalGain: Double by Delegates.notNull()
    private var totalPlusMinusValue: Double by Delegates.notNull()
    private var up: Boolean = false
    private var liquidity: Double by Delegates.notNull()
    private var yieldYear: Double by Delegates.notNull()
    lateinit var yieldYearPerc: String
    lateinit var lastUpdate: String

    lateinit var performance: Performance

    @JsonProperty("chartShareValueColors")
    lateinit var chartColors: String
    @JsonProperty("chartShareValueData")
    lateinit var chartData: String
    @JsonProperty("chartShareValueDate")
    lateinit var chartDate: String
    @JsonProperty("chartShareValueDraw")
    lateinit var chartDraw: String

    lateinit var chartSectorData: String
    lateinit var chartSectorTitle: String
    lateinit var chartSectorDraw: String
    lateinit var chartSectorCompanies: String

    lateinit var chartCapData: String
    lateinit var chartCapTitle: String
    lateinit var chartCapDraw: String
    @JsonProperty("chartCapCompanies")
    lateinit var chartCapCompanies: String

    private var totalVariation: Double by Delegates.notNull()

    var equities: List<Equity> = mutableListOf()
    var shareValues: List<ShareValue> = mutableListOf()
    var accounts: List<Account> = mutableListOf()

    constructor(source: Parcel) : this() {
        readFromParcel(source)
    }

    val isUp: Boolean
        get() = totalGain > 0

    fun getTotalValue(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(totalValue)
    }

    fun getTotalGain(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(totalGain)
    }

    fun getTotalPlusMinusValue(): String {
        return if (totalPlusMinusValue > 0)
            "+" + UserContext.FORMAT_LOCAL_ONE.format(totalPlusMinusValue) + "%"
        else
            UserContext.FORMAT_LOCAL_ONE.format(totalPlusMinusValue) + "%"
    }

    fun getLiquidity(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(liquidity)
    }

    fun getYieldYear(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(yieldYear)
    }

    fun getTotalVariation(): String {
        val plus = if (isTodayUp) "+" else ""
        return plus + UserContext.FORMAT_LOCAL_TWO.format(totalVariation) + "%"
    }

    val isTodayUp: Boolean
        get() = totalVariation >= 0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(totalValue)
        dest.writeDouble(totalGain)
        dest.writeDouble(totalPlusMinusValue)
        dest.writeDouble(liquidity)
        dest.writeByte((if (up) 1 else 0).toByte()) // myBoolean = in.readByte() == 1;
        dest.writeDouble(yieldYear)
        dest.writeString(yieldYearPerc)
        dest.writeString(lastUpdate)

        dest.writeParcelable(performance, flags)

        dest.writeString(chartColors)
        dest.writeString(chartData)
        dest.writeString(chartDate)
        dest.writeString(chartDraw)

        dest.writeString(chartSectorData)
        dest.writeString(chartSectorTitle)
        dest.writeString(chartSectorDraw)
        dest.writeString(chartSectorCompanies)

        dest.writeString(chartCapData)
        dest.writeString(chartCapTitle)
        dest.writeString(chartCapDraw)
        dest.writeString(chartCapCompanies)

        dest.writeTypedList(equities)
        dest.writeTypedList(shareValues)
        dest.writeTypedList(accounts)

        dest.writeDouble(totalVariation)
    }

    private fun readFromParcel(source: Parcel) {
        totalValue = source.readDouble()
        totalGain = source.readDouble()
        totalPlusMinusValue = source.readDouble()
        liquidity = source.readDouble()
        up = source.readByte().toInt() == 1
        yieldYear = source.readDouble()
        yieldYearPerc = source.readString()
        lastUpdate = source.readString()
        performance = source.readParcelable(Performance::class.java.classLoader)
        chartColors = source.readString()
        chartData = source.readString()
        chartDate = source.readString()
        chartDraw = source.readString()
        chartSectorData = source.readString()
        chartSectorTitle = source.readString()
        chartSectorDraw = source.readString()
        chartSectorCompanies = source.readString()
        chartCapData = source.readString()
        chartCapTitle = source.readString()
        chartCapDraw = source.readString()
        chartCapCompanies = source.readString()
        source.readTypedList(equities, Equity.CREATOR)
        source.readTypedList(shareValues, ShareValue.CREATOR)
        source.readTypedList(accounts, Account.CREATOR)
        totalVariation = source.readDouble()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Portfolio> = object : Parcelable.Creator<Portfolio> {
            override fun createFromParcel(source: Parcel): Portfolio {
                return Portfolio(source)
            }

            override fun newArray(size: Int): Array<Portfolio?> {
                return arrayOfNulls(size)
            }
        }
    }
}
