package fr.cph.stock.android;

import android.graphics.Color;

/**
 * Created by carl on 9/18/2017.
 */

public class Constants {

	public static final int GREEN = Color.rgb(0, 160, 0);
	public static final int RED = Color.rgb(160, 0, 0);

	public static final String PREFS_NAME = "StockTracker";
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String PORTFOLIO = "portfolio";

	public static String URL_LOGIN = "?login=";
	public static String URL_PASSWORD = "&password=";

	private Constants() {
	}
}
