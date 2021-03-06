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

package fr.cph.stock.android.web

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient

class DebugWebChromeClient : WebChromeClient() {

    override fun onConsoleMessage(m: ConsoleMessage): Boolean {
        Log.d(TAG, m.lineNumber().toString() + ": " + m.message() + " - " + m.sourceId())
        return true
    }

    companion object {
        private val TAG = "DebugWebChromeClient"
    }
}