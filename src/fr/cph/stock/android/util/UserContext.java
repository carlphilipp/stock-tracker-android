package fr.cph.stock.android.util;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class UserContext {
	public static NumberFormat FORMAT_CURRENCY_ZERO;
	public static NumberFormat FORMAT_CURRENCY_ONE;
	public static NumberFormat FORMAT_LOCAL_ZERO;
	public static NumberFormat FORMAT_LOCAL_ONE;
	public static NumberFormat FORMAT_LOCAL_TWO;

	public static void setup(final Locale locale) {
		FORMAT_CURRENCY_ZERO = NumberFormat.getCurrencyInstance(locale);
		FORMAT_CURRENCY_ZERO.setMaximumFractionDigits(0);
		FORMAT_CURRENCY_ZERO.setMinimumFractionDigits(0);
		FORMAT_CURRENCY_ZERO.setRoundingMode(RoundingMode.HALF_DOWN);

		FORMAT_CURRENCY_ONE = NumberFormat.getCurrencyInstance(locale);
		FORMAT_CURRENCY_ONE.setMaximumFractionDigits(1);
		FORMAT_CURRENCY_ONE.setMinimumFractionDigits(0);
		FORMAT_CURRENCY_ONE.setRoundingMode(RoundingMode.HALF_DOWN);

		FORMAT_LOCAL_ZERO = NumberFormat.getInstance(locale);
		FORMAT_LOCAL_ZERO.setMaximumFractionDigits(0);
		FORMAT_LOCAL_ZERO.setMinimumFractionDigits(0);
		FORMAT_LOCAL_ZERO.setRoundingMode(RoundingMode.HALF_DOWN);

		FORMAT_LOCAL_ONE = NumberFormat.getInstance(locale);
		FORMAT_LOCAL_ONE.setMaximumFractionDigits(1);
		FORMAT_LOCAL_ONE.setMinimumFractionDigits(0);
		FORMAT_LOCAL_ONE.setRoundingMode(RoundingMode.HALF_DOWN);

		FORMAT_LOCAL_TWO = NumberFormat.getInstance(locale);
		FORMAT_LOCAL_TWO.setMaximumFractionDigits(2);
		FORMAT_LOCAL_TWO.setMinimumFractionDigits(0);
		FORMAT_LOCAL_TWO.setRoundingMode(RoundingMode.HALF_DOWN);
	}
}
