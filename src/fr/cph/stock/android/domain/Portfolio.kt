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

import java.util.ArrayList
import java.util.Locale

import fr.cph.stock.android.util.UserContext

import fr.cph.stock.android.util.UserContext.FORMAT_CURRENCY_ZERO
import fr.cph.stock.android.util.UserContext.FORMAT_LOCAL_ONE

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Portfolio : Parcelable {

    var locale: Locale? = null
    private var totalValue: Double? = null
    private var totalGain: Double? = null
    private var totalPlusMinusValue: Double? = null
    private var up: Boolean = false
    private var liquidity: Double? = null
    private var yieldYear: Double? = null
    var yieldYearPerc: String? = null
    var lastUpdate: String? = null

    var performance: Performance? = null

    @JsonProperty("chartShareValueColors")
    var chartColors: String? = null
    @JsonProperty("chartShareValueData")
    var chartData: String? = null
    @JsonProperty("chartShareValueDate")
    var chartDate: String? = null
    @JsonProperty("chartShareValueDraw")
    var chartDraw: String? = null

    var chartSectorData: String? = null
    var chartSectorTitle: String? = null
    var chartSectorDraw: String? = null
    var chartSectorCompanies: String? = null

    var chartCapData: String? = null
    var chartCapTitle: String? = null
    var chartCapDraw: String? = null
    @JsonProperty("chartCapCompanies")
    var chartCapCompanies: String? = null

    private var totalVariation: Double? = null

    var equities: List<Equity> = ArrayList()
    var shareValues: List<ShareValue>? = null
    var accounts: List<Account>? = null

    constructor() {}

    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    val isUp: Boolean
        get() = totalGain!! > 0

    fun getTotalValue(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(totalValue)
    }

    fun setTotalValue(totalValue: Double?) {
        this.totalValue = totalValue
    }

    fun getTotalGain(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(totalGain)
    }

    fun setTotalGain(totalGain: Double?) {
        this.totalGain = totalGain
    }

    fun getTotalPlusMinusValue(): String {
        return if (totalPlusMinusValue!! > 0)
            "+" + UserContext.FORMAT_LOCAL_ONE.format(totalPlusMinusValue!!) + "%"
        else
            UserContext.FORMAT_LOCAL_ONE.format(totalPlusMinusValue!!) + "%"
    }

    fun setTotalPlusMinusValue(totalPlusMinusValue: Double?) {
        this.totalPlusMinusValue = totalPlusMinusValue
    }

    fun getLiquidity(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(liquidity)
    }

    fun setLiquidity(liquidity: Double?) {
        this.liquidity = liquidity
    }

    fun getYieldYear(): String {
        return UserContext.FORMAT_CURRENCY_ZERO.format(yieldYear)
    }

    fun setYieldYear(yieldYear: Double?) {
        this.yieldYear = yieldYear
    }

    fun getTotalVariation(): String {
        val plus = if (isTodayUp) "+" else ""
        return plus + UserContext.FORMAT_LOCAL_TWO.format(totalVariation) + "%"
    }

    fun setTotalVariation(totalVariation: Double?) {
        this.totalVariation = totalVariation
    }

    val isTodayUp: Boolean
        get() = totalVariation!! >= 0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(totalValue!!)
        dest.writeDouble(totalGain!!)
        dest.writeDouble(totalPlusMinusValue!!)
        dest.writeDouble(liquidity!!)
        dest.writeByte((if (up) 1 else 0).toByte()) // myBoolean = in.readByte() == 1;
        dest.writeDouble(yieldYear!!)
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

        dest.writeDouble(totalVariation!!)
    }

    private fun readFromParcel(`in`: Parcel) {
        totalValue = `in`.readDouble()
        totalGain = `in`.readDouble()
        totalPlusMinusValue = `in`.readDouble()
        liquidity = `in`.readDouble()
        up = `in`.readByte().toInt() == 1
        yieldYear = `in`.readDouble()
        yieldYearPerc = `in`.readString()
        lastUpdate = `in`.readString()
        performance = `in`.readParcelable(Performance::class.java.classLoader)
        chartColors = `in`.readString()
        chartData = `in`.readString()
        chartDate = `in`.readString()
        chartDraw = `in`.readString()
        chartSectorData = `in`.readString()
        chartSectorTitle = `in`.readString()
        chartSectorDraw = `in`.readString()
        chartSectorCompanies = `in`.readString()
        chartCapData = `in`.readString()
        chartCapTitle = `in`.readString()
        chartCapDraw = `in`.readString()
        chartCapCompanies = `in`.readString()
        equities = ArrayList()
        `in`.readTypedList(equities, Equity.CREATOR)
        shareValues = ArrayList()
        `in`.readTypedList(shareValues, ShareValue.CREATOR)
        accounts = ArrayList()
        `in`.readTypedList(accounts, Account.CREATOR)
        totalVariation = `in`.readDouble()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Portfolio> = object : Parcelable.Creator<Portfolio> {
            override fun createFromParcel(`in`: Parcel): Portfolio {
                return Portfolio(`in`)
            }

            override fun newArray(size: Int): Array<Portfolio?> {
                return arrayOfNulls(size)
            }
        }
    }
}
