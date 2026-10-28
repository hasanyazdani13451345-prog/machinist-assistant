package com.machinist.assistant.data

/**
 * Workshop reference data for common machining materials.
 * Values are practical guidelines (Vc in m/min, feeds in mm/rev,
 * kc in N/mm^2, fz in mm/tooth, density in g/cm^3).
 */
data class Material(
    val nameFa: String,
    val nameEn: String,
    val vcTurnHss: Double,
    val vcTurnCarbide: Double,
    val vcDrillHss: Double,
    val vcDrillCarbide: Double,
    val kc: Double,
    val feedRough: Double,
    val feedFinish: Double,
    val fz: Double,
    val density: Double
)

data class DensityMaterial(val nameFa: String, val nameEn: String, val density: Double)

object MaterialsData {

    val list: List<Material> = listOf(
        Material("فولاد کم‌کربن (St37)", "Low carbon steel (S235)", 25.0, 120.0, 22.0, 90.0, 1700.0, 0.30, 0.10, 0.12, 7.85),
        Material("فولاد کربنی (C45)", "Medium carbon steel (C45)", 22.0, 110.0, 20.0, 85.0, 1900.0, 0.30, 0.10, 0.10, 7.85),
        Material("فولاد آلیاژی (42CrMo4)", "Alloy steel (42CrMo4)", 18.0, 90.0, 16.0, 70.0, 2100.0, 0.25, 0.08, 0.09, 7.85),
        Material("فولاد ابزار", "Tool steel", 12.0, 70.0, 12.0, 55.0, 2400.0, 0.20, 0.08, 0.07, 7.85),
        Material("استیل آستنیتی (304)", "Austenitic stainless (304)", 15.0, 100.0, 14.0, 80.0, 2000.0, 0.25, 0.10, 0.08, 7.90),
        Material("استیل مارتنزیتی (420)", "Martensitic stainless (420)", 14.0, 90.0, 13.0, 70.0, 2100.0, 0.20, 0.10, 0.08, 7.74),
        Material("چدن خاکستری (GG20)", "Grey cast iron (GG20)", 22.0, 130.0, 20.0, 100.0, 1100.0, 0.30, 0.12, 0.12, 7.20),
        Material("چدن نشکن (GGG50)", "Ductile iron (GGG50)", 15.0, 100.0, 14.0, 80.0, 1300.0, 0.25, 0.10, 0.10, 7.30),
        Material("آلومینیوم", "Aluminium", 100.0, 400.0, 80.0, 250.0, 600.0, 0.35, 0.15, 0.20, 2.70),
        Material("برنج", "Brass", 60.0, 200.0, 50.0, 150.0, 750.0, 0.25, 0.12, 0.12, 8.50),
        Material("برنز", "Bronze", 50.0, 180.0, 45.0, 140.0, 850.0, 0.25, 0.12, 0.10, 8.80),
        Material("مس", "Copper", 50.0, 200.0, 45.0, 150.0, 850.0, 0.25, 0.12, 0.10, 8.96),
        Material("تیتانیوم (Ti6Al4V)", "Titanium (Ti6Al4V)", 12.0, 45.0, 10.0, 35.0, 1700.0, 0.20, 0.08, 0.08, 4.51)
    )

    val weightMaterials: List<DensityMaterial> = listOf(
        DensityMaterial("فولاد", "Steel", 7.85),
        DensityMaterial("استیل", "Stainless steel", 7.90),
        DensityMaterial("چدن", "Cast iron", 7.20),
        DensityMaterial("آلومینیوم", "Aluminium", 2.70),
        DensityMaterial("برنج", "Brass", 8.50),
        DensityMaterial("برنز", "Bronze", 8.80),
        DensityMaterial("مس", "Copper", 8.96),
        DensityMaterial("تیتانیوم", "Titanium", 4.51),
        DensityMaterial("نایلون", "Nylon", 1.15),
        DensityMaterial("تفلون (PTFE)", "PTFE", 2.16),
        DensityMaterial("پلکسی‌گلاس", "Acrylic (PMMA)", 1.18)
    )
}
