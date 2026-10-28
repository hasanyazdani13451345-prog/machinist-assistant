package com.machinist.assistant.data

/**
 * ISO metric screw threads with standard tap drill sizes (mm).
 * Coarse series M1..M68 plus the common fine-pitch series.
 */
data class ThreadRow(val name: String, val pitch: Double, val drill: Double, val fine: Boolean)

object ThreadsData {

    val coarse: List<ThreadRow> = listOf(
        ThreadRow("M1 × 0.25", 0.25, 0.75, false),
        ThreadRow("M1.2 × 0.25", 0.25, 0.95, false),
        ThreadRow("M1.6 × 0.35", 0.35, 1.25, false),
        ThreadRow("M2 × 0.4", 0.4, 1.6, false),
        ThreadRow("M2.5 × 0.45", 0.45, 2.05, false),
        ThreadRow("M3 × 0.5", 0.5, 2.5, false),
        ThreadRow("M3.5 × 0.6", 0.6, 2.9, false),
        ThreadRow("M4 × 0.7", 0.7, 3.3, false),
        ThreadRow("M4.5 × 0.75", 0.75, 3.7, false),
        ThreadRow("M5 × 0.8", 0.8, 4.2, false),
        ThreadRow("M6 × 1", 1.0, 5.0, false),
        ThreadRow("M7 × 1", 1.0, 6.0, false),
        ThreadRow("M8 × 1.25", 1.25, 6.8, false),
        ThreadRow("M10 × 1.5", 1.5, 8.5, false),
        ThreadRow("M12 × 1.75", 1.75, 10.2, false),
        ThreadRow("M14 × 2", 2.0, 12.0, false),
        ThreadRow("M16 × 2", 2.0, 14.0, false),
        ThreadRow("M18 × 2.5", 2.5, 15.5, false),
        ThreadRow("M20 × 2.5", 2.5, 17.5, false),
        ThreadRow("M22 × 2.5", 2.5, 19.5, false),
        ThreadRow("M24 × 3", 3.0, 21.0, false),
        ThreadRow("M27 × 3", 3.0, 24.0, false),
        ThreadRow("M30 × 3.5", 3.5, 26.5, false),
        ThreadRow("M33 × 3.5", 3.5, 29.5, false),
        ThreadRow("M36 × 4", 4.0, 32.0, false),
        ThreadRow("M39 × 4", 4.0, 35.0, false),
        ThreadRow("M42 × 4.5", 4.5, 37.5, false),
        ThreadRow("M45 × 4.5", 4.5, 40.5, false),
        ThreadRow("M48 × 5", 5.0, 43.0, false),
        ThreadRow("M52 × 5", 5.0, 47.0, false),
        ThreadRow("M56 × 5.5", 5.5, 50.5, false),
        ThreadRow("M60 × 5.5", 5.5, 54.5, false),
        ThreadRow("M64 × 6", 6.0, 58.0, false),
        ThreadRow("M68 × 6", 6.0, 62.0, false)
    )

    val fine: List<ThreadRow> = listOf(
        ThreadRow("M6 × 0.75", 0.75, 5.25, true),
        ThreadRow("M8 × 0.75", 0.75, 7.25, true),
        ThreadRow("M8 × 1", 1.0, 7.0, true),
        ThreadRow("M10 × 0.75", 0.75, 9.25, true),
        ThreadRow("M10 × 1", 1.0, 9.0, true),
        ThreadRow("M10 × 1.25", 1.25, 8.75, true),
        ThreadRow("M12 × 1", 1.0, 11.0, true),
        ThreadRow("M12 × 1.25", 1.25, 10.75, true),
        ThreadRow("M12 × 1.5", 1.5, 10.5, true),
        ThreadRow("M14 × 1", 1.0, 13.0, true),
        ThreadRow("M14 × 1.5", 1.5, 12.5, true),
        ThreadRow("M16 × 1", 1.0, 15.0, true),
        ThreadRow("M16 × 1.5", 1.5, 14.5, true),
        ThreadRow("M18 × 1.5", 1.5, 16.5, true),
        ThreadRow("M18 × 2", 2.0, 16.0, true),
        ThreadRow("M20 × 1.5", 1.5, 18.5, true),
        ThreadRow("M20 × 2", 2.0, 18.0, true),
        ThreadRow("M22 × 1.5", 1.5, 20.5, true),
        ThreadRow("M22 × 2", 2.0, 20.0, true),
        ThreadRow("M24 × 1.5", 1.5, 22.5, true),
        ThreadRow("M24 × 2", 2.0, 22.0, true),
        ThreadRow("M27 × 1.5", 1.5, 25.5, true),
        ThreadRow("M27 × 2", 2.0, 25.0, true),
        ThreadRow("M30 × 1.5", 1.5, 28.5, true),
        ThreadRow("M30 × 2", 2.0, 28.0, true),
        ThreadRow("M30 × 3", 3.0, 27.0, true),
        ThreadRow("M33 × 2", 2.0, 31.0, true),
        ThreadRow("M36 × 1.5", 1.5, 34.5, true),
        ThreadRow("M36 × 2", 2.0, 34.0, true),
        ThreadRow("M36 × 3", 3.0, 33.0, true),
        ThreadRow("M39 × 2", 2.0, 37.0, true),
        ThreadRow("M39 × 3", 3.0, 36.0, true),
        ThreadRow("M42 × 2", 2.0, 40.0, true),
        ThreadRow("M42 × 3", 3.0, 39.0, true),
        ThreadRow("M48 × 2", 2.0, 46.0, true),
        ThreadRow("M48 × 3", 3.0, 45.0, true)
    )

    val all: List<ThreadRow> = coarse + fine

    /** Nominal diameter parsed from a designation like "M12 × 1.75". */
    val nominal: (ThreadRow) -> Double = { it.name.substringAfter('M').substringBefore('×').trim().toDouble() }
}
