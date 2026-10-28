package com.machinist.assistant.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.machinist.assistant.databinding.ItemHomeCardBinding
import com.machinist.assistant.databinding.ItemSectionHeaderBinding

data class HomeItem(
    val header: String?,
    val title: String,
    val subtitle: String,
    val icon: Int,
    val target: Class<*>?
)

class HomeAdapter(
    private val items: List<HomeItem>,
    private val onClick: (HomeItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun isHeader(position: Int) = items[position].header != null

    override fun getItemViewType(position: Int) =
        if (items[position].header != null) TYPE_HEADER else TYPE_CARD

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderVh(ItemSectionHeaderBinding.inflate(inf, parent, false))
        } else {
            CardVh(ItemHomeCardBinding.inflate(inf, parent, false))
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is HeaderVh) {
            holder.b.title.text = item.header
        } else if (holder is CardVh) {
            holder.b.title.text = item.title
            holder.b.subtitle.text = item.subtitle
            holder.b.icon.setImageResource(item.icon)
            holder.b.root.setOnClickListener { onClick(item) }
        }
    }

    class HeaderVh(val b: ItemSectionHeaderBinding) : RecyclerView.ViewHolder(b.root)
    class CardVh(val b: ItemHomeCardBinding) : RecyclerView.ViewHolder(b.root)

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_CARD = 1
    }
}
