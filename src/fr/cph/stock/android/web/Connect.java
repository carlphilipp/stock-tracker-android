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

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.charset.Charset;

import fr.cph.stock.android.entity.ResponseDTO;
import fr.cph.stock.android.exception.AppException;

public class Connect {

	private static Connect instance = null;

	private DefaultHttpClient client;
	private ObjectMapper mapper;

	private static final String TAG = "Connect";

	//public static String URL_BASE = "http://192.168.1.145:8080/";
	public static String URL_BASE = "https://www.stocktracker.fr/";
	public static String URL_LOGIN = "?login=";
	public static String URL_PASSWORD = "&password=";


	private String request;

	private Connect() {
		final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
		this.client = new MyHttpClient();
		this.client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		this.client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		this.mapper = new ObjectMapper();
	}

	public static Connect getInstance() {
		if (instance == null) {
			instance = new Connect();
		}
		return instance;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	private String urlBuilder() throws UnsupportedEncodingException {
		return URL_BASE + request;
//		return URL_BASE +URLEncoder.encode(request, "UTF-8")
	}

	private String connectUrl(final String address) throws IOException {
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

	public ResponseDTO getResponse() throws AppException {
		try {
			final String data = connectUrl(urlBuilder());
			return mapper.readValue(data, ResponseDTO.class);
		} catch (IOException e) {
			throw new AppException(e.getMessage(), e);
		}
	}
}
