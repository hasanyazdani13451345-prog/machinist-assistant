package com.machinist.assistant.tables

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.machinist.assistant.R
import com.machinist.assistant.data.ThreadsData
import com.machinist.assistant.databinding.ActivityThreadsBinding
import com.machinist.assistant.ui.SimpleTableAdapter
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.setupToolbar

class ThreadsActivity : AppCompatActivity() {

    private lateinit var b: ActivityThreadsBinding
    private lateinit var adapter: SimpleTableAdapter
    private var fine = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityThreadsBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_threads)

        adapter = SimpleTableAdapter(3, floatArrayOf(1.6f, 1f, 1f))
        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter

        b.tgType.check(R.id.btnCoarse)
        b.tgType.addOnButtonCheckedListener { _, checkedId, checked ->
            if (checked) {
                fine = checkedId == R.id.btnFine
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

    private fun refresh() {
        val q = b.etSearch.text?.toString()?.trim() ?: ""
        val source = if (fine) ThreadsData.fine else ThreadsData.coarse
        val rows = source
            .filter { q.isEmpty() || it.name.contains(q, ignoreCase = true) }
            .map { listOf(it.name, Fmt.n(it.pitch, 2), Fmt.n(it.drill, 2)) }
        val header = listOf(
            getString(R.string.col_thread),
            getString(R.string.col_pitch),
            getString(R.string.col_drill)
        )
        adapter.submit(listOf(header) + rows)
    }
}
