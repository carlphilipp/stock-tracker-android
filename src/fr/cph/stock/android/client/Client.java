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

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.charset.Charset;

import fr.cph.stock.android.entity.ResponseDTO;
import fr.cph.stock.android.exception.AppException;

public class Client {

	private static final String TAG = "Client";

	//public static String URL_BASE = "http://192.168.1.145:8080/";
	private static String URL_BASE = "https://www.stocktracker.fr/";

	private static Client instance = null;

	private DefaultHttpClient client;
	private ObjectMapper mapper;

	private Client() {
		final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
		client = new HttpsClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		mapper = new ObjectMapper();
	}

	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	private String get(final String address) throws IOException {
		Log.d(TAG, "address: " + address);
		final HttpResponse response = client.execute(new HttpGet(address));
		InputStream in = null;
		try {
			in = response.getEntity().getContent();
			return IOUtils.toString(in, Charset.forName("UTF8"));
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public ResponseDTO getResponse(final String url) throws AppException {
		try {
			final String data = get(URL_BASE + url);
			Log.d(TAG, "Response: " + data);
			return mapper.readValue(data, ResponseDTO.class);
		} catch (final IOException e) {
			throw new AppException(e.getMessage(), e);
		}
	}
}
