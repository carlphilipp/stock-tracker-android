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
import fr.cph.stock.android.util.UserContext
import kotlin.properties.Delegates

class ShareValue constructor() : Parcelable {

    lateinit var date: String
    lateinit var account: String
    private var portfolioValue: Double by Delegates.notNull()
    private var shareQuantity: Double by Delegates.notNull()
    private var shareValue: Double by Delegates.notNull()
    private var monthlyYield: Double by Delegates.notNull()
    private var up: Boolean = false
    var commentary: String? = null

    constructor(source: Parcel) : this() {
        readFromParcel(source)
    }

    fun getShareValue(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(shareValue)
    }

    val isUp: Boolean
        get() = shareValue > 100

    fun getPortfolioValue(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(portfolioValue)
    }

    fun getShareQuantity(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(shareQuantity)
    }

    fun getMonthlyYield(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(monthlyYield)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(date)
        dest.writeString(account)
        dest.writeDouble(portfolioValue)
        dest.writeDouble(shareQuantity)
        dest.writeDouble(shareValue)
        dest.writeDouble(monthlyYield)
        dest.writeString(commentary)
        dest.writeByte((if (up) 1 else 0).toByte()) // myBoolean = in.readByte() == 1;
    }

    private fun readFromParcel(source: Parcel) {
        date = source.readString()
        account = source.readString()
        portfolioValue = source.readDouble()
        shareQuantity = source.readDouble()
        shareValue = source.readDouble()
        monthlyYield = source.readDouble()
        commentary = source.readString()
        up = source.readByte().toInt() == 1
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShareValue> = object : Parcelable.Creator<ShareValue> {
            override fun createFromParcel(source: Parcel): ShareValue {
                return ShareValue(source)
            }

            override fun newArray(size: Int): Array<ShareValue?> {
                return arrayOfNulls(size)
            }
        }
    }
}
