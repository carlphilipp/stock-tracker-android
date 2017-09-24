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
import kotlin.properties.Delegates

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Equity constructor() : Parcelable {

    lateinit var name: String
    private var unitCostPrice: Double by Delegates.notNull()
    private var value: Double by Delegates.notNull()
    private var plusMinusValue: Double by Delegates.notNull()
    private var up: Boolean = false
    var isUpVariation: Boolean = false
    private var quantity: Double by Delegates.notNull()
    private var yieldUnitCostPrice: Double by Delegates.notNull()
    private var quote: Double by Delegates.notNull()
    private var plusMinusUnitCostPriceValue: Double by Delegates.notNull()
    private var variation: String? = null

    constructor(source: Parcel) : this() {
        readFromParcel(source)
    }

    fun getQuantity(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(quantity)
    }

    fun getYieldUnitCostPrice(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(yieldUnitCostPrice) + "%"
    }

    fun getQuote(): String {
        return UserContext.FORMAT_LOCAL_TWO.format(quote)
    }

    fun getPlusMinusUnitCostPriceValue(): String {
        return if (plusMinusValue > 0)
            "+" + UserContext.FORMAT_LOCAL_ZERO.format(plusMinusUnitCostPriceValue)
        else
            UserContext.FORMAT_LOCAL_ZERO.format(plusMinusUnitCostPriceValue)
    }

    fun getUnitCostPrice(): String {
        return UserContext.FORMAT_LOCAL_TWO.format(unitCostPrice)
    }

    fun getValue(): String {
        return UserContext.FORMAT_LOCAL_ZERO.format(value)
    }

    fun getPlusMinusValue(): String {
        return if (plusMinusValue > 0)
            "+" + UserContext.FORMAT_LOCAL_ONE.format(plusMinusValue) + "%"
        else
            UserContext.FORMAT_LOCAL_ONE.format(plusMinusValue) + "%"
    }

    val isUp: Boolean
        get() = plusMinusValue > 0

    override fun describeContents(): Int {
        return 0
    }

    fun getVariation(): String {
        return if (variation == null) "?" else variation!!
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeDouble(unitCostPrice)
        dest.writeDouble(value)
        dest.writeDouble(plusMinusValue)
        dest.writeDouble(quantity)
        dest.writeDouble(yieldUnitCostPrice)
        dest.writeDouble(quote)
        dest.writeDouble(plusMinusUnitCostPriceValue)
        dest.writeByte((if (up) 1 else 0).toByte())
        dest.writeString(variation)
        dest.writeByte((if (isUpVariation) 1 else 0).toByte())
    }

    private fun readFromParcel(source: Parcel) {
        name = source.readString()
        unitCostPrice = source.readDouble()
        value = source.readDouble()
        plusMinusValue = source.readDouble()
        quantity = source.readDouble()
        yieldUnitCostPrice = source.readDouble()
        quote = source.readDouble()
        plusMinusUnitCostPriceValue = source.readDouble()
        up = source.readByte().toInt() == 1
        variation = source.readString()
        isUpVariation = source.readByte().toInt() == 1
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Equity> = object : Parcelable.Creator<Equity> {
            override fun createFromParcel(source: Parcel): Equity {
                return Equity(source)
            }

            override fun newArray(size: Int): Array<Equity?> {
                return arrayOfNulls(size)
            }
        }
    }
}
