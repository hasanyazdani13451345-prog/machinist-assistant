package com.machinist.assistant.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.machinist.assistant.R
import com.machinist.assistant.databinding.ItemTableRowBinding
import com.machinist.assistant.util.tableCell

/**
 * Generic table adapter: renders a header row plus data rows of fixed column count.
 * Columns use optional custom weights; header cells are bold on a tinted background.
 */
class SimpleTableAdapter(
    private val columnCount: Int,
    private val weights: FloatArray? = null,
    private var rows: List<List<String>> = emptyList() // first row = header
) : RecyclerView.Adapter<SimpleTableAdapter.RowVh>() {

    fun submit(newRows: List<List<String>>) {
        rows = newRows
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowVh {
        val b = ItemTableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowVh(b)
    }

    override fun getItemCount() = rows.size

    override fun onBindViewHolder(holder: RowVh, position: Int) {
        val cells = rows[position]
        val ctx = holder.b.root.context
        val header = position == 0
        holder.b.root.removeAllViews()
        for (c in 0 until columnCount) {
            val w = weights?.getOrNull(c) ?: (1f / columnCount)
            val tv = tableCell(ctx, cells.getOrElse(c) { "" }, w, header)
            if (header) {
                tv.setBackgroundColor(ContextCompat.getColor(ctx, R.color.surface_variant))
                tv.setTextColor(ContextCompat.getColor(ctx, R.color.on_surface))
            } else if (position % 2 == 0) {
                tv.setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.transparent))
            }
            holder.b.root.addView(tv)
        }
        if (!header) {
            holder.b.root.background = ContextCompat.getDrawable(ctx, R.drawable.table_divider)
        }
    }

    class RowVh(val b: ItemTableRowBinding) : RecyclerView.ViewHolder(b.root)
}
