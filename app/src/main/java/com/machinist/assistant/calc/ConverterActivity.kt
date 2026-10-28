package com.machinist.assistant.calc

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.machinist.assistant.R
import com.machinist.assistant.databinding.ActivityConverterBinding
import com.machinist.assistant.util.Fmt
import com.machinist.assistant.util.dropdown
import com.machinist.assistant.util.setupToolbar

class ConverterActivity : AppCompatActivity() {

    private class UnitDef(val symbol: String, val factor: Double)

    private class Category(val labelRes: Int, val units: List<UnitDef>, val temperature: Boolean = false)

    private lateinit var b: ActivityConverterBinding
    private var categoryIndex = 0
    private var fromIndex = 0
    private var toIndex = 1

    private val categories = listOf(
        Category(R.string.cat_length, listOf(
            UnitDef("mm", 1.0), UnitDef("cm", 10.0), UnitDef("m", 1000.0),
            UnitDef("inch", 25.4), UnitDef("ft", 304.8)
        )),
        Category(R.string.cat_speed, listOf(
            UnitDef("m/min", 1.0), UnitDef("ft/min", 0.3048), UnitDef("m/s", 60.0), UnitDef("mm/s", 0.06)
        )),
        Category(R.string.cat_feedrev, listOf(
            UnitDef("mm/rev", 1.0), UnitDef("in/rev", 25.4)
        )),
        Category(R.string.cat_feedmin, listOf(
            UnitDef("mm/min", 1.0), UnitDef("in/min", 25.4), UnitDef("m/min", 1000.0)
        )),
        Category(R.string.cat_power, listOf(
            UnitDef("kW", 1.0), UnitDef("W", 0.001), UnitDef("HP (metric)", 0.7355), UnitDef("HP (mech)", 0.7457)
        )),
        Category(R.string.cat_torque, listOf(
            UnitDef("N·m", 1.0), UnitDef("kgf·m", 9.80665), UnitDef("lb·ft", 1.35582)
        )),
        Category(R.string.cat_mass, listOf(
            UnitDef("kg", 1.0), UnitDef("g", 0.001), UnitDef("t", 1000.0), UnitDef("lb", 0.4535924)
        )),
        Category(R.string.cat_angle, listOf(
            UnitDef("°", 1.0), UnitDef("rad", 57.2957795), UnitDef("grad", 0.9)
        )),
        Category(R.string.cat_temp, listOf(
            UnitDef("°C", 0.0), UnitDef("°F", 0.0)
        ), temperature = true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.toolbarInclude.toolbar.setupToolbar(this, R.string.nav_converter)

        val actvCat = b.tilCategory.dropdown(categories) { getString(it.labelRes) }
        actvCat.setText(getString(categories[0].labelRes), false)
        actvCat.setOnItemClickListener { _, _, pos, _ ->
            categoryIndex = pos
            fromIndex = 0
            toIndex = 1
            refreshUnits()
        }

        b.actvFrom.setOnItemClickListener { _, _, pos, _ ->
            fromIndex = pos
            convert()
        }
        b.actvTo.setOnItemClickListener { _, _, pos, _ ->
            toIndex = pos
            convert()
        }

        b.btnSwap.setOnClickListener {
            val f = fromIndex
            fromIndex = toIndex
            toIndex = f
            val fromText = b.actvFrom.text?.toString() ?: ""
            val toText = b.actvTo.text?.toString() ?: ""
            b.actvFrom.setText(toText, false)
            b.actvTo.setText(fromText, false)
            convert()
        }

        b.etValue.doAfterTextChanged { convert() }

        refreshUnits()
    }

    private fun refreshUnits() {
        val units = categories[categoryIndex].units
        val actvFrom = b.tilFrom.dropdown(units) { it.symbol }
        val actvTo = b.tilTo.dropdown(units) { it.symbol }
        fromIndex = fromIndex.coerceIn(0, units.size - 1)
        toIndex = toIndex.coerceIn(0, units.size - 1)
        if (fromIndex == toIndex && units.size > 1) toIndex = (toIndex + 1) % units.size
        actvFrom.setText(units[fromIndex].symbol, false)
        actvTo.setText(units[toIndex].symbol, false)
        b.cardResult.visibility = View.GONE
        convert()
    }

    private fun convert() {
        val cat = categories[categoryIndex]
        val value = Fmt.parse(b.etValue.text?.toString())
        if (value == null) {
            b.cardResult.visibility = View.GONE
            return
        }
        val from = cat.units[fromIndex]
        val to = cat.units[toIndex]

        val result: Double = if (cat.temperature) {
            convertTemperature(value, from.symbol, to.symbol)
        } else {
            value * from.factor / to.factor
        }

        b.cardResult.visibility = View.VISIBLE
        b.tvResultLabel.text = Fmt.n(value, 4) + " " + from.symbol
        b.tvResult.text = Fmt.n(result, resultLetDigits(result))
        b.tvResultUnit.text = to.symbol
    }

    private fun resultLetDigits(r: Double): Int = when {
        r >= 1000 -> 1
        r >= 1 -> 3
        else -> 4
    }

    private fun convertTemperature(v: Double, from: String, to: String): Double = when {
        from == "°C" && to == "°F" -> v * 9.0 / 5.0 + 32.0
        from == "°F" && to == "°C" -> (v - 32.0) * 5.0 / 9.0
        else -> v
    }
}
