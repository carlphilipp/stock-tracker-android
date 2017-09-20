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

package fr.cph.stock.android.client;

import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import fr.cph.stock.android.entity.ResponseDTO;
import fr.cph.stock.android.enumtype.UrlType;
import fr.cph.stock.android.exception.AppException;

public class Client {

	private static final String TAG = "Client";

	//public static String URL_BASE = "http://192.168.1.145:8080/";
	private static String SCHEME = "https";
	private static String BASE_URL = "www.stocktracker.fr";
	private static String AUTH_PATH = "authmobile";

	private static Client instance = null;

	private ObjectMapper mapper;

	private Client() {
		final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
		mapper = new ObjectMapper();
	}

	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	public ResponseDTO getResponse(final UrlType urlType, final Map<String, String> params) throws AppException {
		try {
			final Uri.Builder builder = new Uri.Builder().scheme(SCHEME)
					.authority(BASE_URL)
					.appendPath(urlType.getUrl());
			params.forEach(builder::appendQueryParameter);
			Log.d(TAG, "Request: " + builder.toString());
			URL url = new URL(builder.toString());
			final String data = getContent(url);
			Log.d(TAG, "Response: " + data);
			return mapper.readValue(data, ResponseDTO.class);
		} catch (final IOException e) {
			throw new AppException(e.getMessage(), e);
		}
	}

	private String getContent(final URL url) throws IOException {
		InputStream stream = null;
		HttpsURLConnection connection = null;
		String result = null;
		try {
			connection = (HttpsURLConnection) url.openConnection();
			connection.setReadTimeout(3000);
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();
			// Retrieve the response body as an InputStream.
			stream = connection.getInputStream();
			if (stream != null) {
				result = IOUtils.toString(stream, Charset.forName("UTF8"));
			}
		} finally {
			// Close Stream and disconnect HTTPS connection.
			if (stream != null) {
				stream.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return result;
	}
}
