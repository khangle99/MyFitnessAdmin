package com.khangle.myfitnessadmin.report.mostviewmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.excercise.exclist.ExcerciseListAdapter
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.Menu
import com.khangle.myfitnessadmin.nutrition.menuList.MenuListAdapter
import com.khangle.myfitnessadmin.report.mostusedexcercise.MostUseExcerciewViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MostViewMenuActivity : AppCompatActivity() {

    private  val viewmodel: MostViewMenuViewModel by viewModels()

    lateinit var adapter: MenuListAdapter
    lateinit var excerciseList: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var horizontalChart: HorizontalBarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_most_view_menu)

        setupUI()
        viewmodel.allMenu.observe(this) {
            progressBar.visibility = View.INVISIBLE
            if (it.isEmpty()) {
                Toast.makeText(baseContext, "Empty List", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
            setupChart(it)
        }
        viewmodel.fetchAllMenu()
    }

    private fun setupUI() {
        excerciseList = findViewById(R.id.allMenuRecyclerview)
        progressBar = findViewById(R.id.most_view_menu_progress)
        horizontalChart = findViewById(R.id.most_view_menu_chart)

        adapter = MenuListAdapter {
        }
        excerciseList.adapter = adapter
        excerciseList.layoutManager = LinearLayoutManager(this)
    }

    private fun setupChart(menuList: List<Menu>) {
        val entries = mutableListOf<BarEntry>()


        menuList.asReversed().forEachIndexed { index, exc ->
            entries.add(BarEntry(index.toFloat(), exc.view.toFloat()))
        }
        val dataSet = BarDataSet(entries, "Views")
        dataSet.color = R.color.design_default_color_on_primary

        val barSpace = 0.02f
        val barWidth = 0.35f
        val groupSpace = 0.25f

        val valueFormater = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                if ( index >= 0 && index < menuList.size) {
                    return menuList[index].name
                }
                return ""
            }
        }
        val bardata = BarData(dataSet)

        horizontalChart.data = bardata
        bardata.barWidth = barWidth
        horizontalChart.axisRight.isInverted = true
        horizontalChart.xAxis.valueFormatter = valueFormater
        horizontalChart.xAxis.granularity = 1f
        horizontalChart.fitScreen()
        horizontalChart.description.isEnabled = false
        horizontalChart.setDrawGridBackground(false)
        horizontalChart.axisRight.isEnabled = false
        horizontalChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        horizontalChart.invalidate()
    }
}