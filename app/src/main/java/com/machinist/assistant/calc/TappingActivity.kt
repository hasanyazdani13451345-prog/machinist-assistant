package com.machinist.assistant.calc

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.machinist.assistant.R
import com.machinist.assistant.data.DrillsData
import com.machinist.assistant.data.Material
import com.machinist.assistant.data.MaterialsData
import com.machinist.assistant.data.ThreadsData
import com.machinist.assistant.databinding.ActivityTappingBinding
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.currentLangIsFa
import com.machinist.assistant.util.dropdown
import com.machinist.assistant.util.setupToolbar
import kotlin.math.PI

class TappingActivity : AppCompatActivity() {

    private lateinit var b: ActivityTappingBinding
    private var material: Material = MaterialsData.list[1]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTappingBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_tapping)

        val actvThread = b.tilThread.dropdown(ThreadsData.all) { it.name }
        actvThread.setText(ThreadsData.all[12].name, false) // M8 × 1.25
        actvThread.setOnItemClickListener { _, _, _, _ -> compute() }

        val actv = b.tilMaterial.dropdown(MaterialsData.list) { displayName(it) }
        actv.setText(displayName(material), false)
        actv.setOnItemClickListener { _, _, pos, _ ->
            material = MaterialsData.list[pos]
            compute()
        }

        b.tgMode.check(R.id.btnModeTable)
        b.tgMode.addOnButtonCheckedListener { _, checkedId, checked ->
            if (checked) {
                val tableMode = checkedId == R.id.btnModeTable
                b.secTable.visibility = if (tableMode) View.VISIBLE else View.GONE
                b.secManual.visibility = if (tableMode) View.GONE else View.VISIBLE
            }
        }

        b.btnCalc.setOnClickListener { compute() }
    }

    private fun displayName(m: Material): String =
        if (currentLangIsFa()) m.nameFa else m.nameEn

    private fun compute() {
        val fromTable = b.tgMode.checkedButtonId == R.id.btnModeTable

        val nominal: Double
        val drill: Double
        if (fromTable) {
            val selectedName = b.actvThread.text?.toString() ?: return
            val row = ThreadsData.all.firstOrNull { it.name == selectedName } ?: return
            nominal = ThreadsData.nominal(row)
            drill = row.drill
        } else {
            val d = Fmt.parse(b.etD.text?.toString())
            val p = Fmt.parse(b.etP.text?.toString())
            if (d == null || p == null || d <= 0 || p <= 0 || p >= d) {
                Toast.makeText(this, R.string.err_input, Toast.LENGTH_SHORT).show()
                return
            }
            nominal = d
            drill = d - p
        }

        // Taps are typically HSS; cutting speed reduced to 60% of drilling.
        val vcTap = material.vcDrillHss * 0.6
        val n = 1000.0 * vcTap / (PI * nominal)

        // 1 decimal when the drill is a multiple of 0.1 (e.g. 6.8), else 2 (e.g. 5.25)
        val digits = if (Math.abs(drill * 10.0 - Math.round(drill * 10.0)) < 1e-9) 1 else 2
        b.cardResult.visibility = View.VISIBLE
        b.tvDrill.text = Fmt.n(drill, digits)
        b.tvStdDrill.text = Fmt.n(DrillsData.nearestStandard(drill), 1) + " mm"
        b.tvRpm.text = Fmt.int(n) + " rpm"
    }
}
