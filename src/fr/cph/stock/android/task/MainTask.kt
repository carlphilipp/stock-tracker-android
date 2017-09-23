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

package fr.cph.stock.android.task

import android.os.AsyncTask
import android.util.Log

import org.json.JSONObject

import fr.cph.stock.android.client.Client
import fr.cph.stock.android.domain.Portfolio
import fr.cph.stock.android.domain.ResponseDTO
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.exception.AppException
import fr.cph.stock.android.util.UserContext

class MainTask//FIXME logout does not seem to be working
(private val `object`: Any, private val urlType: UrlType, private val params: Map<String, String>) : AsyncTask<Void, Void, Boolean>() {
    private var responseDTO: ResponseDTO? = null
    private var error: String? = null

    override fun doInBackground(vararg params: Void): Boolean? {
        var toReturn = true
        try {
            responseDTO = Client.getResponse(urlType, this.params)
        } catch (e: AppException) {
            Log.e(TAG, e.message, e)
            this.error = e.message
            toReturn = false
        }

        if (responseDTO != null) {
            val errorMessage = responseDTO!!.error
            if (errorMessage != null && errorMessage != "") {
                this.error = errorMessage
                toReturn = false
            }
        }
        return toReturn
    }

    override fun onPostExecute(success: Boolean?) {
        try {
            val clazz = `object`.javaClass
            if (success!!) {
                UserContext.setup(responseDTO!!.portfolio!!.locale!!)
                when (urlType) {
                    UrlType.LOGOUT -> {
                        Log.d(TAG, "logout: " + clazz.name)
                        clazz.getMethod("logOut").invoke(`object`)
                    }
                    UrlType.UPDATEHISTORY -> clazz.getMethod("reloadData", Portfolio::class.java).invoke(`object`, responseDTO!!.portfolio)
                    UrlType.AUTH -> clazz.getMethod("loadHome", Portfolio::class.java).invoke(`object`, responseDTO!!.portfolio)
                    UrlType.RELOAD -> clazz.getMethod("reloadData", Portfolio::class.java).invoke(`object`, responseDTO!!.portfolio)
                }
            } else {
                val jsonError = JSONObject()
                jsonError.accumulate("error", error)
                clazz.getMethod("displayError", JSONObject::class.java).invoke(`object`, jsonError)
            }
        } catch (e: Exception) {
            Log.e(TAG, "", e)
        }

        super.onPostExecute(success)
    }

    companion object {

        private val TAG = "MainTask"
    }
}
