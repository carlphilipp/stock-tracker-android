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
class Account constructor() : Parcelable {

    lateinit var id: String
    lateinit var name: String
    lateinit var currency: String
    private var liquidity: Double? = null

    constructor(source: Parcel) : this() {
        readFromParcel(source)
    }

    fun getLiquidity(): String {
        return UserContext.FORMAT_LOCAL_ONE.format(liquidity)
    }

    fun setLiquidity(liquidity: Double?) {
        this.liquidity = liquidity
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(currency)
        dest.writeDouble(liquidity!!)
    }

    private fun readFromParcel(`in`: Parcel) {
        id = `in`.readString()
        name = `in`.readString()
        currency = `in`.readString()
        liquidity = `in`.readDouble()
    }

    override fun toString(): String {
        return name + " " + currency + " " + getLiquidity()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Account> = object : Parcelable.Creator<Account> {
            override fun createFromParcel(source: Parcel): Account {
                return Account(source)
            }

            override fun newArray(size: Int): Array<Account?> {
                return arrayOfNulls(size)
            }
        }
    }
}
