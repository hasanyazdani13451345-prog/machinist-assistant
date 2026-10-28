package com.machinist.assistant.util

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout

/** True when the per-app language is Persian (defaults to system language). */
fun currentLangIsFa(): Boolean {
    val tag = androidx.appcompat.app.AppCompatDelegate.getApplicationLocales().toLanguageTags()
    return if (tag.isBlank()) java.util.Locale.getDefault().language == "fa" else tag.startsWith("fa")
}

/** Toolbar wiring helper for all screens. */
fun MaterialToolbar.setupToolbar(activity: AppCompatActivity, titleRes: Int, up: Boolean = true) {
    activity.setSupportActionBar(this)
    activity.supportActionBar?.title = activity.getString(titleRes)
    if (up) {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setNavigationOnClickListener { activity.finish() }
    }
}

/** Attach a non-editable dropdown (ExposedDropdownMenu pattern). Returns the field. */
fun <T> TextInputLayout.dropdown(items: List<T>, label: (T) -> String): AutoCompleteTextView {
    val actv = editText as? AutoCompleteTextView
        ?: throw IllegalStateException("TextInputLayout must contain AutoCompleteTextView")
    actv.setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, items.map(label)))
    return actv
}

/** Build a table cell TextView for [SimpleTableAdapter]. */
fun tableCell(context: Context, text: String, weight: Float, header: Boolean): TextView {
    val tv = TextView(context)
    tv.text = text
    tv.textSize = if (header) 12f else 13f
    tv.setPadding(10, 8, 10, 8)
    tv.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight)
    if (header) tv.setTypeface(null, Typeface.BOLD)
    return tv
}

fun View.visibleOr(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
