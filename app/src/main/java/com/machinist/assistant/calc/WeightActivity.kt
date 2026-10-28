package com.machinist.assistant.calc

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.machinist.assistant.R
import com.machinist.assistant.data.DensityMaterial
import com.machinist.assistant.data.MaterialsData
import com.machinist.assistant.databinding.ActivityWeightBinding
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.currentLangIsFa
import com.machinist.assistant.util.dropdown
import com.machinist.assistant.util.setupToolbar
import kotlin.math.PI

class WeightActivity : AppCompatActivity() {

    private class Param(val key: String, val labelRes: Int, val default: String)

    private data class Shape(val labelRes: Int, val params: List<Param>)

    private lateinit var b: ActivityWeightBinding
    private var material: DensityMaterial = MaterialsData.weightMaterials[0]
    private var shapeIndex = 0
    private val inputs = mutableMapOf<String, EditText>()

    private val shapes = listOf(
        Shape(R.string.shape_round, listOf(
            Param("d", R.string.param_d, "20"),
            Param("l", R.string.param_l, "100")
        )),
        Shape(R.string.shape_tube, listOf(
            Param("od", R.string.param_od, "30"),
            Param("t", R.string.param_t, "3"),
            Param("l", R.string.param_l, "100")
        )),
        Shape(R.string.shape_plate, listOf(
            Param("w", R.string.param_w, "50"),
            Param("t", R.string.param_thk, "10"),
            Param("l", R.string.param_l, "100")
        )),
        Shape(R.string.shape_hex, listOf(
            Param("a", R.string.param_a, "19"),
            Param("l", R.string.param_l, "100")
        ))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_weight)

        val actvShape = b.tilShape.dropdown(shapes) { getString(it.labelRes) }
        actvShape.setText(getString(shapes[0].labelRes), false)
        actvShape.setOnItemClickListener { _, _, pos, _ ->
            shapeIndex = pos
            rebuildParams()
        }

        val actv = b.tilMaterial.dropdown(MaterialsData.weightMaterials) { displayName(it) }
        actv.setText(displayName(material), false)
        actv.setOnItemClickListener { _, _, pos, _ ->
            material = MaterialsData.weightMaterials[pos]
            updateDensityLabel()
            compute()
        }

        updateDensityLabel()
        rebuildParams()
    }

    private fun displayName(m: DensityMaterial): String =
        if (currentLangIsFa()) m.nameFa else m.nameEn

    private fun updateDensityLabel() {
        b.tvDensity.text = getString(R.string.density_note, Fmt.n(material.density, 2))
    }

    private fun rebuildParams() {
        b.paramsContainer.removeAllViews()
        inputs.clear()
        val inflater = layoutInflater
        for (p in shapes[shapeIndex].params) {
            val til = inflater.inflate(
                R.layout.item_param_input, b.paramsContainer, false
            ) as TextInputLayout
            til.hint = getString(p.labelRes)
            val et = til.editText as TextInputEditText
            et.setText(p.default)
            et.doAfterTextChanged { compute() }
            b.paramsContainer.addView(til)
            inputs[p.key] = et
        }
        compute()
    }

    private fun value(key: String): Double? = Fmt.parse(inputs[key]?.text?.toString())

    private fun compute() {
        val volumeMm3: Double? = when (shapeIndex) {
            0 -> {
                val d = value("d"); val l = value("l")
                if (d == null || l == null || d <= 0 || l <= 0) null
                else PI / 4.0 * d * d * l
            }
            1 -> {
                val od = value("od"); val t = value("t"); val l = value("l")
                if (od == null || t == null || l == null || od <= 0 || t <= 0 || l <= 0) null
                else {
                    val id = od - 2 * t
                    if (id <= 0) null
                    else PI / 4.0 * (od * od - id * id) * l
                }
            }
            2 -> {
                val w = value("w"); val t = value("t"); val l = value("l")
                if (w == null || t == null || l == null || w <= 0 || t <= 0 || l <= 0) null
                else w * t * l
            }
            else -> {
                val a = value("a"); val l = value("l")
                if (a == null || l == null || a <= 0 || l <= 0) null
                else 0.8660254 * a * a * l
            }
        }

        if (volumeMm3 == null) {
            b.cardResult.visibility = View.GONE
            return
        }

        val volumeCm3 = volumeMm3 / 1000.0
        val massG = volumeCm3 * material.density
        val massKg = massG / 1000.0

        b.cardResult.visibility = View.VISIBLE
        b.tvMassKg.text = Fmt.n(massKg, 3)
        b.tvVolume.text = Fmt.n(volumeCm3, 1) + " cm³"
        b.tvMassG.text = Fmt.n(massG, 0) + " g"
    }
}
