/**
 * Copyright 2013 Carl-Philipp Harmant
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.cph.stock.android.web;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

	private static final String TAG = "Md5";

	private final String password;

	public Md5(final String password) {
		this.password = password;
	}

	public String getHexInString() {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
		} catch (NoSuchAlgorithmException e) {
			Log.i(TAG, e.getMessage());
		}

		// convert the byte to hex format method 2
		final StringBuilder hexString = new StringBuilder();
		byte byteData[] = md.digest();
		for (byte aByteData : byteData) {
			String hex = Integer.toHexString(0xff & aByteData);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
