package com.machinist.assistant

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.machinist.assistant.databinding.ActivityGuideBinding
import com.machinist.assistant.util.setupToolbar

class GuideActivity : AppCompatActivity() {

    private lateinit var b: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_guide)

        val items = resources.getStringArray(R.array.guide_items)
        for (html in items) {
            val tv = TextView(this)
            tv.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tv.textSize = 14f
            tv.setLineSpacing(0f, 1.15f)
            tv.setTextColor(getColor(R.color.on_surface))
            tv.setPadding(8, 12, 8, 12)
            b.guideContainer.addView(
                tv,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { gravity = Gravity.START }
            )
        }
    }
}
