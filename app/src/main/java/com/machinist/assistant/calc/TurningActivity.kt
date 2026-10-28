package com.machinist.assistant.calc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.machinist.assistant.R
import com.machinist.assistant.data.Material
import com.machinist.assistant.data.MaterialsData
import com.machinist.assistant.databinding.ActivityTurningBinding
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.currentLangIsFa
import com.machinist.assistant.util.dropdown
import com.machinist.assistant.util.setupToolbar
import kotlin.math.PI

class TurningActivity : AppCompatActivity() {

    private lateinit var b: ActivityTurningBinding
    private var material: Material = MaterialsData.list[1]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTurningBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_turning)

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
        if (b.etFeed.text.isNullOrBlank()) b.etFeed.setText(Fmt.n(material.feedRough, 2))
        b.tvVcHint.text = getString(R.string.hint_vc_suggest, Fmt.n(vc, 0)) +
                "  •  " + getString(R.string.hint_feed_suggest, Fmt.n(material.feedRough, 2))
    }

    private fun compute() {
        val d = Fmt.parse(b.etD.text?.toString())
        val vc = Fmt.parse(b.etVc.text?.toString())
        val f = Fmt.parse(b.etFeed.text?.toString())
        val ap = Fmt.parse(b.etAp.text?.toString())
        val len = Fmt.parse(b.etL.text?.toString())
        val eff = (Fmt.parse(b.etEff.text?.toString()) ?: 80.0) / 100.0

        if (d == null || vc == null || f == null || ap == null || d <= 0 || vc <= 0 || f <= 0 || ap <= 0) {
            Toast.makeText(this, R.string.err_input, Toast.LENGTH_SHORT).show()
            return
        }

        val n = 1000.0 * vc / (PI * d)            // rpm
        val vf = f * n                            // mm/min
        val mrr = vc * f * ap                     // cm3/min
        val pc = material.kc * ap * f * vc / 60000.0  // kW
        val pm = pc / eff
        val time = if (len != null && len > 0 && vf > 0) len / vf else null // min

        b.cardResult.visibility = android.view.View.VISIBLE
        b.tvRpm.text = Fmt.int(n)
        b.tvVf.text = Fmt.n(vf, 1)
        b.tvMrr.text = Fmt.n(mrr, 1)
        b.tvPower.text = Fmt.n(pc, 2)
        b.tvMotor.text = Fmt.n(pm, 2)
        b.tvTime.text = time?.let { Fmt.time(it) + " min" } ?: "—"
    }
}
