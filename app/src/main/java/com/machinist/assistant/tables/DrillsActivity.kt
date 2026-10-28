package com.machinist.assistant.tables

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.machinist.assistant.R
import com.machinist.assistant.data.DrillsData
import com.machinist.assistant.databinding.ActivityDrillsBinding
import com.machinist.assistant.ui.SimpleTableAdapter
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.setupToolbar

class DrillsActivity : AppCompatActivity() {

    private lateinit var b: ActivityDrillsBinding
    private lateinit var adapter: SimpleTableAdapter
    private var metric = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDrillsBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_drills)

        rebuildAdapter()

        b.tgType.check(R.id.btnInch)
        b.tgType.addOnButtonCheckedListener { _, checkedId, checked ->
            if (checked) {
                metric = checkedId == R.id.btnMetric
                rebuildAdapter()
                refresh()
            }
        }

        b.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b2: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b2: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) { refresh() }
        })

        refresh()
    }

    private fun rebuildAdapter() {
        adapter = if (metric) {
            SimpleTableAdapter(2, floatArrayOf(1f, 1f))
        } else {
            SimpleTableAdapter(3, floatArrayOf(1.2f, 1f, 1f))
        }
        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter
    }

    private fun refresh() {
        val q = b.etSearch.text?.toString()?.trim() ?: ""
        val header: List<String>
        val rows: List<List<String>>
        if (metric) {
            header = listOf(getString(R.string.col_mm), getString(R.string.col_inch))
            rows = DrillsData.metric
                .filter { q.isEmpty() || Fmt.n(it.mm, 1).contains(q) }
                .map { listOf(Fmt.n(it.mm, 1), Fmt.n(it.decimalInch, 4)) }
        } else {
            header = listOf(
                getString(R.string.col_inch),
                getString(R.string.col_dec),
                getString(R.string.col_mm)
            )
            rows = DrillsData.inch
                .filter { q.isEmpty() || it.fraction.contains(q, ignoreCase = true) }
                .map { listOf(it.fraction, Fmt.n(it.decimalInch, 4), Fmt.n(it.mm, 3)) }
        }
        adapter.submit(listOf(header) + rows)
    }
}
