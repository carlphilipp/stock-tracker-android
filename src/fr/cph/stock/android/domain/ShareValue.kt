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

class ShareValue : Parcelable {

    var date: String? = null
    var account: String? = null
    private var portfolioValue: Double? = null
    private var shareQuantity: Double? = null
    private var shareValue: Double? = null
    private var monthlyYield: Double? = null
    private var up: Boolean = false
    var commentary: String? = null

    constructor() {}

    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    fun getShareValue(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(shareValue)
    }

    fun setShareValue(shareValue: Double?) {
        this.shareValue = shareValue
    }

    val isUp: Boolean
        get() = shareValue!! > 100

    fun getPortfolioValue(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(portfolioValue)
    }

    fun setPortfolioValue(portfolioValue: Double?) {
        this.portfolioValue = portfolioValue
    }

    fun getShareQuantity(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(shareQuantity)
    }

    fun setShareQuantity(shareQuantity: Double?) {
        this.shareQuantity = shareQuantity
    }

    fun getMonthlyYield(): String {
        return UserContext.FORMAT_CURRENCY_ONE.format(monthlyYield)
    }

    fun setMonthlyYield(monthlyYield: Double?) {
        this.monthlyYield = monthlyYield
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(date)
        dest.writeString(account)
        dest.writeDouble(portfolioValue!!)
        dest.writeDouble(shareQuantity!!)
        dest.writeDouble(shareValue!!)
        dest.writeDouble(monthlyYield!!)
        dest.writeString(commentary)
        dest.writeByte((if (up) 1 else 0).toByte()) // myBoolean = in.readByte() == 1;
    }

    private fun readFromParcel(`in`: Parcel) {
        date = `in`.readString()
        account = `in`.readString()
        portfolioValue = `in`.readDouble()
        shareQuantity = `in`.readDouble()
        shareValue = `in`.readDouble()
        monthlyYield = `in`.readDouble()
        commentary = `in`.readString()
        up = `in`.readByte().toInt() == 1

    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ShareValue> = object : Parcelable.Creator<ShareValue> {
            override fun createFromParcel(`in`: Parcel): ShareValue {
                return ShareValue(`in`)
            }

            override fun newArray(size: Int): Array<ShareValue?> {
                return arrayOfNulls(size)
            }
        }
    }

}
