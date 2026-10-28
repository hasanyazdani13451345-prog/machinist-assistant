package com.machinist.assistant.data

/**
 * Drill size charts.
 * - Inch: fractional drills from 1/64" to 1" with mm equivalents.
 * - Metric: standard drills from 0.3 to 20 mm in 0.1 steps with inch equivalents.
 */
object DrillsData {

    data class InchDrill(val fraction: String, val decimalInch: Double, val mm: Double)
    data class MetricDrill(val mm: Double, val decimalInch: Double)

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

    val inch: List<InchDrill> = (1..64).map { n ->
        val g = gcd(n, 64)
        val frac = "${n / g}/${64 / g}"
        val dec = n / 64.0
        InchDrill(frac, dec, Math.round(dec * 25.4 * 1000.0) / 1000.0)
    }

    val metric: List<MetricDrill> = (3..200).map { v ->
        val mm = v / 10.0
        MetricDrill(mm, Math.round(mm / 25.4 * 10000.0) / 10000.0)
    }

    /** Nearest standard metric drill (0.1 steps), never larger than [size]. */
    fun nearestStandard(size: Double): Double {
        val r = Math.round(size * 10.0) / 10.0
        return if (r > size && r > 0.3) r - 0.1 else r
    }
}
