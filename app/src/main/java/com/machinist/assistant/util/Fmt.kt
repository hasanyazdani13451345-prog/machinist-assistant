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

    fun parse(text: String?): Double? {
        val s = text?.trim() ?: return null
        if (s.isEmpty()) return null
        // Normalize Persian/Arabic-Indic digits and separators so that
        // keyboards producing "۱۲٬۵" or "12,5" still parse correctly.
        val sb = StringBuilder(s.length)
        for (c in s) {
            when (c) {
                in '\u06F0'..'\u06F9' -> sb.append('0' + (c - '\u06F0'))   // Persian ۰-۹
                in '\u0660'..'\u0669' -> sb.append('0' + (c - '\u0660'))   // Arabic-Indic ٠-٩
                '\u066B' -> sb.append('.')                               // Arabic decimal sep
                '\u066C' -> {}                                           // Arabic thousands sep
                else -> sb.append(c)
            }
        }
        return sb.toString().replace(',', '.').toDoubleOrNull()
    }
}
