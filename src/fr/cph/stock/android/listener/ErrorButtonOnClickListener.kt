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

package fr.cph.stock.android.listener

import android.view.View
import fr.cph.stock.android.Constants.LOGIN
import fr.cph.stock.android.Constants.PASSWORD
import fr.cph.stock.android.activity.ErrorActivity
import fr.cph.stock.android.domain.UrlType
import fr.cph.stock.android.task.MainTask

class ErrorButtonOnClickListener(private val errorActivity: ErrorActivity, private val login: String, private val password: String) : View.OnClickListener {

    override fun onClick(v: View) {
        MainTask(errorActivity, UrlType.AUTH, hashMapOf(LOGIN to login, PASSWORD to password)).execute(null as Void?)
    }
}
