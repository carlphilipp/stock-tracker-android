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

import fr.cph.stock.android.util.UserContext

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Equity : Parcelable {

    var name: String? = null
    private var unitCostPrice: Double? = null
    private var value: Double? = null
    private var plusMinusValue: Double? = null
    private var up: Boolean = false
    var isUpVariation: Boolean = false
    private var quantity: Double? = null
    private var yieldYear: Double? = null
    private var yieldUnitCostPrice: Double? = null
    private var quote: Double? = null
    private var plusMinusUnitCostPriceValue: Double? = null
    private var variation: String? = null

    constructor() {}

    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    fun getQuantity(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(quantity)
    }

    fun setQuantity(quantity: Double?) {
        this.quantity = quantity
    }

    fun getYieldYear(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(yieldYear) + "%"
    }

    fun setYieldYear(yieldYear: Double?) {
        this.yieldYear = yieldYear
    }

    fun getYieldUnitCostPrice(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(yieldUnitCostPrice) + "%"
    }

    fun setYieldUnitCostPrice(yieldUnitCostPrice: Double?) {
        this.yieldUnitCostPrice = yieldUnitCostPrice
    }

    fun getQuote(): String {
        return UserContext.FORMAT_LOCAL_TWO.format(quote)
    }

    fun setQuote(quote: Double?) {
        this.quote = quote
    }

    fun getPlusMinusUnitCostPriceValue(): String {
        return if (plusMinusValue!! > 0)
            "+" + UserContext.FORMAT_LOCAL_ZERO.format(plusMinusUnitCostPriceValue)
        else
            UserContext.FORMAT_LOCAL_ZERO.format(plusMinusUnitCostPriceValue)
    }

    fun setPlusMinusUnitCostPriceValue(plusMinusUnitCostPriceValue: Double?) {
        this.plusMinusUnitCostPriceValue = plusMinusUnitCostPriceValue
    }

    fun getUnitCostPrice(): String {
        return UserContext.FORMAT_LOCAL_TWO.format(unitCostPrice)
    }

    fun setUnitCostPrice(unitCostPrice: Double?) {
        this.unitCostPrice = unitCostPrice
    }

    fun getValue(): String {
        return UserContext.FORMAT_LOCAL_ZERO.format(value)
    }

    fun setValue(value: Double?) {
        this.value = value
    }

    fun getPlusMinusValue(): String {
        return if (plusMinusValue!! > 0)
            "+" + UserContext.FORMAT_LOCAL_ONE.format(plusMinusValue!!) + "%"
        else
            UserContext.FORMAT_LOCAL_ONE.format(plusMinusValue!!) + "%"
    }

    fun setPlusMinusValue(plusMinusValue: Double?) {
        this.plusMinusValue = plusMinusValue
    }

    val isUp: Boolean
        get() = plusMinusValue!! > 0

    override fun describeContents(): Int {
        return 0
    }

    fun getVariation(): String {
        return if (variation == null) "?" else variation!!
    }

    fun setVariation(variation: String?) {
        this.variation = variation
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeDouble(unitCostPrice!!)
        dest.writeDouble(value!!)
        dest.writeDouble(plusMinusValue!!)
        dest.writeDouble(quantity!!)
        dest.writeDouble(yieldYear!!)
        dest.writeDouble(yieldUnitCostPrice!!)
        dest.writeDouble(quote!!)
        dest.writeDouble(plusMinusUnitCostPriceValue!!)
        dest.writeByte((if (up) 1 else 0).toByte()) // myBoolean = in.readByte() == 1;
        dest.writeString(variation)
        dest.writeByte((if (isUpVariation) 1 else 0).toByte())
    }

    private fun readFromParcel(`in`: Parcel) {
        name = `in`.readString()
        unitCostPrice = `in`.readDouble()
        value = `in`.readDouble()
        plusMinusValue = `in`.readDouble()
        quantity = `in`.readDouble()
        yieldYear = `in`.readDouble()
        yieldUnitCostPrice = `in`.readDouble()
        quote = `in`.readDouble()
        plusMinusUnitCostPriceValue = `in`.readDouble()
        up = `in`.readByte().toInt() == 1
        variation = `in`.readString()
        isUpVariation = `in`.readByte().toInt() == 1
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Equity> = object : Parcelable.Creator<Equity> {
            override fun createFromParcel(`in`: Parcel): Equity {
                return Equity(`in`)
            }

            override fun newArray(size: Int): Array<Equity?> {
                return arrayOfNulls(size)
            }
        }
    }

}
