package com.machinist.assistant.calc

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.machinist.assistant.R
import com.machinist.assistant.data.Material
import com.machinist.assistant.data.MaterialsData
import com.machinist.assistant.databinding.ActivityDrillingBinding
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.currentLangIsFa
import com.machinist.assistant.util.dropdown
import com.machinist.assistant.util.setupToolbar
import kotlin.math.PI
import kotlin.math.min

class DrillingActivity : AppCompatActivity() {

    private lateinit var b: ActivityDrillingBinding
    private var material: Material = MaterialsData.list[1]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDrillingBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_drilling)

        val actv = b.tilMaterial.dropdown(MaterialsData.list) { displayName(it) }
        actv.setText(displayName(material), false)
        actv.setOnItemClickListener { _, _, pos, _ ->
            material = MaterialsData.list[pos]
            applySuggestions()
        }

        b.tgTool.check(R.id.btnHss)
        b.tgTool.addOnButtonCheckedListener { _, _, _ -> applySuggestions() }

        b.btnCalc.setOnClickListener { compute() }
        applySuggestions()
    }

    private fun displayName(m: Material): String =
        if (currentLangIsFa()) m.nameFa else m.nameEn

    private fun carbide(): Boolean = b.tgTool.checkedButtonId == R.id.btnCarbide

    private fun suggestedFeed(d: Double): Double {
        val base = 0.02 * d
        return min(0.5, maxOf(0.05, base))
    }

    private fun applySuggestions() {
        val vc = if (carbide()) material.vcDrillCarbide else material.vcDrillHss
        if (b.etVc.text.isNullOrBlank()) b.etVc.setText(Fmt.n(vc, 0))
        val d = Fmt.parse(b.etD.text?.toString())
        if (d != null && d > 0 && b.etFeed.text.isNullOrBlank()) {
            b.etFeed.setText(Fmt.n(suggestedFeed(d), 2))
        }
        b.tvFeedHint.text = getString(R.string.hint_vc_suggest, Fmt.n(vc, 0)) +
                "  •  " + getString(
            R.string.hint_feed_suggest,
            Fmt.n(suggestedFeed(d ?: 10.0), 2)
        )
    }

    private fun compute() {
        val d = Fmt.parse(b.etD.text?.toString())
        val vc = Fmt.parse(b.etVc.text?.toString())
        val f = Fmt.parse(b.etFeed.text?.toString())
        val len = Fmt.parse(b.etL.text?.toString())

        if (d == null || vc == null || f == null || d <= 0 || vc <= 0 || f <= 0) {
            Toast.makeText(this, R.string.err_input, Toast.LENGTH_SHORT).show()
            return
        }

        val n = 1000.0 * vc / (PI * d)                        // rpm
        val vf = f * n                                        // mm/min
        val mrr = PI * d * d / 4.0 * f * n / 1000.0           // cm3/min
        val time = if (len != null && len > 0 && vf > 0) (len + 0.3 * d) / vf else null

        b.cardResult.visibility = View.VISIBLE
        b.tvRpm.text = Fmt.int(n)
        b.tvVf.text = Fmt.n(vf, 1)
        b.tvMrr.text = Fmt.n(mrr, 2)
        b.tvTime.text = time?.let { Fmt.time(it) + " min" } ?: "—"
        b.tvPeck.visibility = if (len != null && len > 3 * d) View.VISIBLE else View.GONE
    }
}
