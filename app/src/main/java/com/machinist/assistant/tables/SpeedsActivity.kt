package com.machinist.assistant.tables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.machinist.assistant.R
import com.machinist.assistant.data.MaterialsData
import com.machinist.assistant.databinding.ActivitySpeedsBinding
import com.machinist.assistant.ui.SimpleTableAdapter
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.currentLangIsFa
import com.machinist.assistant.util.setupToolbar

class SpeedsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivitySpeedsBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_speeds)

        val rows = mutableListOf(
            listOf(
                getString(R.string.col_material),
                getString(R.string.col_turn_hss),
                getString(R.string.col_turn_carb),
                getString(R.string.col_drill_hss),
                getString(R.string.col_feed)
            )
        )
        for (m in MaterialsData.list) {
            val name = if (currentLangIsFa()) m.nameFa else m.nameEn
            rows.add(
                listOf(
                    name,
                    Fmt.int(m.vcTurnHss),
                    Fmt.int(m.vcTurnCarbide),
                    Fmt.int(m.vcDrillHss),
                    Fmt.n(m.feedRough, 2) + " – " + Fmt.n(m.feedFinish, 2)
                )
            )
        }

        val adapter = SimpleTableAdapter(5, floatArrayOf(1.8f, 1f, 1f, 1f, 1.3f))
        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter
        adapter.submit(rows)
    }
}
