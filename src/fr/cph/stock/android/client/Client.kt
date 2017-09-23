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
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.io.InputStream
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URL
import java.nio.charset.Charset
import java.util.function.BiConsumer
import javax.net.ssl.HttpsURLConnection

object Client {

    private val TAG = "Client"
    private val mapper: ObjectMapper

    init {
        val cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(cookieManager)
        mapper = ObjectMapper()
    }

    @Throws(AppException::class)
    fun getResponse(urlType: UrlType, params: Map<String, String>): ResponseDTO {
        try {
            val builder = Uri.Builder().scheme("https")
                    .authority("www.stocktracker.fr")
                    .appendPath(urlType.url)
            params.forEach(BiConsumer<String, String> { key, value -> builder.appendQueryParameter(key, value) })
            Log.d(TAG, "Request: " + builder.toString())
            val url = URL(builder.toString())
            val data = getContent(url)
            Log.d(TAG, "Response: " + data!!)
            return mapper.readValue(data, ResponseDTO::class.java)
        } catch (e: IOException) {
            throw AppException(e.message!!, e)
        }

    }

    @Throws(IOException::class)
    private fun getContent(url: URL): String? {
        var stream: InputStream? = null
        var connection: HttpsURLConnection? = null
        var result: String? = null
        try {
            connection = url.openConnection() as HttpsURLConnection
            connection.readTimeout = 3000
            connection.connectTimeout = 3000
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.connect()
            // Retrieve the response body as an InputStream.
            stream = connection.inputStream
            if (stream != null) {
                result = IOUtils.toString(stream, Charset.forName("UTF8"))
            }
        } finally {
            IOUtils.closeQuietly(stream)
            if (connection != null) {
                connection.disconnect()
            }
        }
        return result
    }
}
