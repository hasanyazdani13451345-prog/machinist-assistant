package com.machinist.assistant

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.machinist.assistant.calc.ConverterActivity
import com.machinist.assistant.calc.DrillingActivity
import com.machinist.assistant.calc.MillingActivity
import com.machinist.assistant.calc.TappingActivity
import com.machinist.assistant.calc.TurningActivity
import com.machinist.assistant.calc.WeightActivity
import com.machinist.assistant.databinding.ActivityMainBinding
import com.machinist.assistant.tables.DrillsActivity
import com.machinist.assistant.tables.FitsActivity
import com.machinist.assistant.tables.SpeedsActivity
import com.machinist.assistant.tables.ThreadsActivity
import com.machinist.assistant.ui.HomeAdapter
import com.machinist.assistant.ui.HomeItem
import com.machinist.assistant.util.setupToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.app_name, up = false)

        val adapter = HomeAdapter(buildItems()) { item ->
            item.target?.let { startActivity(Intent(this, it)) }
        }
        b.recycler.layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (adapter.isHeader(position)) 2 else 1
            }
        }
        b.recycler.adapter = adapter
    }

    private fun buildItems(): List<HomeItem> {
        fun card(titleRes: Int, subRes: Int, icon: Int, target: Class<*>) = HomeItem(
            null, getString(titleRes), getString(subRes), icon, target
        )
        return listOf(
            HomeItem(getString(R.string.section_calcs), "", "", 0, null),
            card(R.string.nav_turning, R.string.sub_turning, R.drawable.ic_gear, TurningActivity::class.java),
            card(R.string.nav_milling, R.string.sub_milling, R.drawable.ic_grid, MillingActivity::class.java),
            card(R.string.nav_drilling, R.string.sub_drilling, R.drawable.ic_arrow_down, DrillingActivity::class.java),
            card(R.string.nav_tapping, R.string.sub_tapping, R.drawable.ic_target, TappingActivity::class.java),
            HomeItem(getString(R.string.section_tools), "", "", 0, null),
            card(R.string.nav_converter, R.string.sub_converter, R.drawable.ic_swap, ConverterActivity::class.java),
            card(R.string.nav_weight, R.string.sub_weight, R.drawable.ic_scale, WeightActivity::class.java),
            HomeItem(getString(R.string.section_tables), "", "", 0, null),
            card(R.string.nav_threads, R.string.sub_threads, R.drawable.ic_bolt, ThreadsActivity::class.java),
            card(R.string.nav_speeds, R.string.sub_speeds, R.drawable.ic_speed, SpeedsActivity::class.java),
            card(R.string.nav_fits, R.string.sub_fits, R.drawable.ic_dashboard, FitsActivity::class.java),
            card(R.string.nav_drills, R.string.sub_drills, R.drawable.ic_ruler, DrillsActivity::class.java),
            HomeItem(getString(R.string.section_more), "", "", 0, null),
            card(R.string.nav_guide, R.string.sub_guide, R.drawable.ic_book, GuideActivity::class.java)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefs = MachinistApp.prefs(this)
        return when (item.itemId) {
            R.id.action_theme -> {
                val toDark = prefs.getString(MachinistApp.KEY_NIGHT, "dark") != "dark"
                prefs.edit().putString(MachinistApp.KEY_NIGHT, if (toDark) "dark" else "light").apply()
                AppCompatDelegate.setDefaultNightMode(
                    if (toDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
                true
            }
            R.id.action_language -> {
                val isFa = AppCompatDelegate.getApplicationLocales().toLanguageTags().startsWith("fa")
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(if (isFa) "en" else "fa")
                )
                true
            }
            R.id.action_about -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.about_text))
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
