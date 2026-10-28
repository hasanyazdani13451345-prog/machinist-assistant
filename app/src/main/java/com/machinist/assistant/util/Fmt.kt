package com.machinist.assistant.util

import java.util.Locale
import kotlin.math.roundToInt

object Fmt {

    fun n(v: Double, digits: Int = 2): String = String.format(Locale.US, "%.${digits}f", v)

    fun int(v: Double): String = String.format(Locale.US, "%.0f", v)

    /** Minutes to "m:ss" */
    fun time(minutes: Double): String {
        val totalSec = (minutes * 60).roundToInt()
        val m = totalSec / 60
        val s = totalSec % 60
        return String.format(Locale.US, "%d:%02d", m, s)
    }

    fun parse(text: String?): Double? =
        text?.trim()?.replace(',', '.')?.toDoubleOrNull()
}
