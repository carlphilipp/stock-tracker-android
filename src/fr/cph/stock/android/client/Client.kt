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

package fr.cph.stock.android.client

import android.net.Uri
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import fr.cph.stock.android.domain.ResponseDTO
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.exception.AppException
import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URL

object Client {

    private val TAG = "Client"
    private val mapper: ObjectMapper = ObjectMapper()

    init {
        val cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(cookieManager)
    }

    @Throws(AppException::class)
    fun getResponse(urlType: UrlType, params: Map<String, String>): ResponseDTO {
        return try {
            val builder = Uri.Builder().scheme("https")
                    .authority("www.stocktracker.fr")
                    .appendPath(urlType.url)
            params.forEach({ key, value -> builder.appendQueryParameter(key, value) })
            Log.d(TAG, "Request: " + builder.toString())
            val data = URL(builder.toString()).readText()
            Log.d(TAG, "Response: " + data)
            mapper.readValue(data, ResponseDTO::class.java)
        } catch (e: IOException) {
            throw AppException(e.message!!, e)
        }
    }
}
