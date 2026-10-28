package com.machinist.assistant.tables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.machinist.assistant.R
import com.machinist.assistant.data.FitsData
import com.machinist.assistant.databinding.ActivityFitsBinding
import com.machinist.assistant.ui.SimpleTableAdapter
import com.machinist.assistant.util.dropdown
import com.machinist.assistant.util.setupToolbar

class FitsActivity : AppCompatActivity() {

    private lateinit var b: ActivityFitsBinding
    private lateinit var adapter: SimpleTableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFitsBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_fits)

        adapter = SimpleTableAdapter(5, floatArrayOf(1.1f, 1f, 1f, 1.1f, 1.2f))
        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter

        val actv = b.tilRange.dropdown(FitsData.rangeLabels) { it }
        actv.setText(FitsData.rangeLabels[0], false)
        actv.setOnItemClickListener { _, _, pos, _ -> refresh(pos) }

        refresh(0)
    }

    private fun refresh(rangeIndex: Int) {
        val types = Triple(
            getString(R.string.type_clearance),
            getString(R.string.type_transition),
            getString(R.string.type_interference)
        )
        val rows = mutableListOf(
            listOf(
                getString(R.string.col_fit),
                getString(R.string.col_hole),
                getString(R.string.col_shaft),
                getString(R.string.col_type),
                getString(R.string.col_gap)
            )
        )
        for (r in FitsData.rows(rangeIndex, types)) {
            rows.add(listOf(r.name, r.holeTol, r.shaftTol, r.type, r.gap))
        }
        adapter.submit(rows)
    }
}
