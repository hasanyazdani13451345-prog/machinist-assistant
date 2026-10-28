package com.machinist.assistant.data

/**
 * ISO 286 hole-basis fits for the 10 most used nominal size ranges.
 * All values are in micrometers (µm).
 * g/f/e use upper deviation (es) for shafts, k/n/p use lower deviation (ei).
 */
object FitsData {

    data class FitRow(
        val name: String,
        val holeTol: String,
        val shaftTol: String,
        val type: String,
        val gap: String
    )

    val rangeLabels: List<String> = listOf(
        "1 – 3", "3 – 6", "6 – 10", "10 – 18", "18 – 30",
        "30 – 50", "50 – 80", "80 – 120", "120 – 180", "180 – 250"
    )

    // IT grades per range index (µm)
    private val IT6 = intArrayOf(6, 8, 9, 11, 13, 16, 19, 22, 25, 29)
    private val IT7 = intArrayOf(10, 12, 15, 18, 21, 25, 30, 35, 40, 46)
    private val IT8 = intArrayOf(14, 18, 22, 27, 33, 39, 46, 54, 63, 72)

    // Fundamental deviations (µm)
    private val gES = intArrayOf(-2, -4, -5, -6, -7, -9, -10, -12, -14, -15)
    private val kEI = intArrayOf(0, 1, 1, 1, 2, 2, 2, 3, 3, 4)
    private val nEI = intArrayOf(4, 8, 10, 12, 15, 17, 20, 23, 27, 31)
    private val pEI = intArrayOf(6, 12, 15, 18, 22, 26, 32, 37, 43, 50)
    private val fES = intArrayOf(-6, -10, -13, -16, -20, -25, -30, -36, -43, -50)
    private val eES = intArrayOf(-14, -20, -25, -32, -40, -50, -60, -72, -85, -100)

    private fun signed(v: Int): String = if (v >= 0) "+$v" else "$v"

    private fun build(name: String, itHole: Int, shaftUpper: Int, shaftLower: Int, typeLabels: Triple<String, String, String>): FitRow {
        // Hole band is 0 .. +itHole (H7/H8 basis)
        val minGap = 0 - shaftUpper      // smallest hole minus biggest shaft
        val maxGap = itHole - shaftLower // biggest hole minus smallest shaft
        val type = when {
            maxGap <= 0 -> typeLabels.third   // no clearance possible (incl. line-to-line)
            minGap < 0 -> typeLabels.second
            else -> typeLabels.first
        }
        return FitRow(
            name,
            "0 / " + signed(itHole),
            signed(shaftUpper) + " / " + signed(shaftLower),
            type,
            signed(minGap) + " … " + signed(maxGap)
        )
    }

    fun rows(
        rangeIndex: Int,
        typeLabels: Triple<String, String, String> // clearance, transition, interference
    ): List<FitRow> {
        val i = rangeIndex.coerceIn(0, 9)
        val it6 = IT6[i]; val it7 = IT7[i]; val it8 = IT8[i]
        return listOf(
            build("H7/g6", it7, gES[i], gES[i] - it6, typeLabels),
            build("H7/h6", it7, 0, -it6, typeLabels),
            build("H8/f7", it8, fES[i], fES[i] - it7, typeLabels),
            build("H7/e7", it7, eES[i], eES[i] - it7, typeLabels),
            build("H7/k6", it7, kEI[i] + it6, kEI[i], typeLabels),
            build("H7/n6", it7, nEI[i] + it6, nEI[i], typeLabels),
            build("H7/p6", it7, pEI[i] + it6, pEI[i], typeLabels)
        )
    }
}
