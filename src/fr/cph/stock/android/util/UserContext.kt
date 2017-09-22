package fr.cph.stock.android.util

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object UserContext {
    lateinit var FORMAT_CURRENCY_ZERO: NumberFormat
    lateinit var FORMAT_CURRENCY_ONE: NumberFormat
    lateinit var FORMAT_LOCAL_ZERO: NumberFormat
    lateinit var FORMAT_LOCAL_ONE: NumberFormat
    lateinit var FORMAT_LOCAL_TWO: NumberFormat

    fun setup(locale: Locale) {
        FORMAT_CURRENCY_ZERO = NumberFormat.getCurrencyInstance(locale)
        FORMAT_CURRENCY_ZERO.maximumFractionDigits = 0
        FORMAT_CURRENCY_ZERO.minimumFractionDigits = 0
        FORMAT_CURRENCY_ZERO.roundingMode = RoundingMode.HALF_DOWN

        FORMAT_CURRENCY_ONE = NumberFormat.getCurrencyInstance(locale)
        FORMAT_CURRENCY_ONE.maximumFractionDigits = 1
        FORMAT_CURRENCY_ONE.minimumFractionDigits = 0
        FORMAT_CURRENCY_ONE.roundingMode = RoundingMode.HALF_DOWN

        FORMAT_LOCAL_ZERO = NumberFormat.getInstance(locale)
        FORMAT_LOCAL_ZERO.maximumFractionDigits = 0
        FORMAT_LOCAL_ZERO.minimumFractionDigits = 0
        FORMAT_LOCAL_ZERO.roundingMode = RoundingMode.HALF_DOWN

        FORMAT_LOCAL_ONE = NumberFormat.getInstance(locale)
        FORMAT_LOCAL_ONE.maximumFractionDigits = 1
        FORMAT_LOCAL_ONE.minimumFractionDigits = 0
        FORMAT_LOCAL_ONE.roundingMode = RoundingMode.HALF_DOWN

        FORMAT_LOCAL_TWO = NumberFormat.getInstance(locale)
        FORMAT_LOCAL_TWO.maximumFractionDigits = 2
        FORMAT_LOCAL_TWO.minimumFractionDigits = 0
        FORMAT_LOCAL_TWO.roundingMode = RoundingMode.HALF_DOWN
    }
}
