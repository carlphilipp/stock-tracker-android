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

import java.security.MessageDigest

class Md5(private val password: String) {
    val hexInString: String
        get() {
            val md = MessageDigest.getInstance("MD5")
            md.update(password.toByteArray())

            val hexString = StringBuilder()
            val byteData: ByteArray = md!!.digest()
            byteData.forEach { byte ->
                run {
                    val hex = Integer.toHexString(0xff and byte.toInt())
                    if (hex.length == 1)
                        hexString.append('0')
                    hexString.append(hex)
                }
            }
            return hexString.toString()
        }
}
