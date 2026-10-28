package com.machinist.assistant.calc

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.machinist.assistant.R
import com.machinist.assistant.data.Material
import com.machinist.assistant.data.MaterialsData
import com.machinist.assistant.databinding.ActivityMillingBinding
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.currentLangIsFa
import com.machinist.assistant.util.dropdown
import com.machinist.assistant.util.setupToolbar
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.sqrt

class MillingActivity : AppCompatActivity() {

    private lateinit var b: ActivityMillingBinding
    private var material: Material = MaterialsData.list[1]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMillingBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_milling)

        val actv = b.tilMaterial.dropdown(MaterialsData.list) { displayName(it) }
        actv.setText(displayName(material), false)
        actv.setOnItemClickListener { _, _, pos, _ ->
            material = MaterialsData.list[pos]
            applySuggestions()
        }

        b.tgTool.check(R.id.btnCarbide)
        b.tgTool.addOnButtonCheckedListener { _, _, _ -> applySuggestions() }

        b.btnCalc.setOnClickListener { compute() }
        applySuggestions()
    }

    private fun displayName(m: Material): String =
        if (currentLangIsFa()) m.nameFa else m.nameEn

    private fun carbide(): Boolean = b.tgTool.checkedButtonId == R.id.btnCarbide

    private fun applySuggestions() {
        val vc = if (carbide()) material.vcTurnCarbide else material.vcTurnHss
        if (b.etVc.text.isNullOrBlank()) b.etVc.setText(Fmt.n(vc, 0))
        if (b.etFz.text.isNullOrBlank()) b.etFz.setText(Fmt.n(material.fz, 3))
        b.tvFzHint.text = getString(R.string.hint_fz, Fmt.n(material.fz, 3)) +
                "  •  " + getString(R.string.hint_vc_suggest, Fmt.n(vc, 0))
    }

    private fun compute() {
        val dc = Fmt.parse(b.etDc.text?.toString())
        val z = Fmt.parse(b.etZ.text?.toString())
        val vc = Fmt.parse(b.etVc.text?.toString())
        val fz = Fmt.parse(b.etFz.text?.toString())
        val ap = Fmt.parse(b.etAp.text?.toString())
        val ae = Fmt.parse(b.etAe.text?.toString())
        val len = Fmt.parse(b.etL.text?.toString())

        if (dc == null || z == null || vc == null || fz == null || ap == null || ae == null ||
            dc <= 0 || z <= 0 || vc <= 0 || fz <= 0 || ap <= 0 || ae <= 0
        ) {
            Toast.makeText(this, R.string.err_input, Toast.LENGTH_SHORT).show()
            return
        }

        val n = 1000.0 * vc / (PI * dc)                 // rpm
        val vf = fz * z * n                             // mm/min
        val mrr = ap * ae * vf / 1000.0                 // cm3/min
        val lc = if (ae >= dc) dc / 2.0 else sqrt(max(0.0, ae * (dc - ae)))
        val time = if (len != null && len > 0 && vf > 0) (len + lc) / vf else null

        b.cardResult.visibility = View.VISIBLE
        b.tvRpm.text = Fmt.int(n)
        b.tvVf.text = Fmt.n(vf, 1)
        b.tvMrr.text = Fmt.n(mrr, 1)
        b.tvTime.text = time?.let { Fmt.time(it) + " min" } ?: "—"
    }
}
