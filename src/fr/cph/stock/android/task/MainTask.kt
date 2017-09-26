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
import fr.cph.stock.android.Constants.ERROR
import fr.cph.stock.android.activity.StockTrackerActivity
import fr.cph.stock.android.client.Client
import fr.cph.stock.android.domain.ResponseDTO
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.exception.AppException
import fr.cph.stock.android.util.UserContext
import org.json.JSONObject

//FIXME logout does not seem to be working
class MainTask(private val activity: StockTrackerActivity, private val urlType: UrlType, private val params: Map<String, String>) : AsyncTask<Void, Void, ResponseDTO>() {

    override fun doInBackground(vararg params: Void): ResponseDTO {
        return try {
            Client.getResponse(urlType, this.params)
        } catch (e: AppException) {
            Log.e(TAG, e.message, e)
            val responseDTOError = ResponseDTO()
            responseDTOError.error = e.message
            responseDTOError
        }
    }

    override fun onPostExecute(responseDTO: ResponseDTO) {
        try {
            if (responseDTO.error == null) {
                UserContext.setup(responseDTO.portfolio.locale)
                when (urlType) {
                    UrlType.LOGOUT -> activity.logOut()
                    else -> activity.update(responseDTO.portfolio)
                }
            } else {
                activity.displayError(JSONObject().accumulate(ERROR, responseDTO.error))
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }
        super.onPostExecute(responseDTO)
    }

    companion object {
        private val TAG = "MainTask"
    }
}
