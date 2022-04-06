package com.example.statisticsexample;

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.statisticsexample.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener





class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var tvSocket: TextView
    lateinit var barChart: BarChart
    private val valueList = ArrayList<Double>()
    val currency = Currency(Data("live_trades_btcusd"), "bts:subscribe")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        tvSocket = binding.tvSocket
        barChart = binding.barChartView
        initBarChart()
        connectSocket(currency)



    }

    private fun initBarChart() {
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false)
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false)
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false)

        //remove the description label text located at the lower right corner
        val description = Description()
        description.setEnabled(false)
        barChart.description = description

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000)
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000)
        val xAxis = barChart.xAxis
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set the horizontal distance of the grid line
        xAxis.granularity = 1f
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)
        val leftAxis = barChart.axisLeft
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false)
        val rightAxis = barChart.axisRight
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false)
        val legend = barChart.legend
        //setting the shape of the legend form to line, default square shape
        legend.form = Legend.LegendForm.LINE
        //setting the text size of the legend
        legend.textSize = 11f
        //setting the alignment of legend toward the chart
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        //setting the stacking direction of legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false)
    }

    private fun initBarDataSet(barDataSet: BarDataSet) {    //Changing the color of the bar
        barDataSet.color = Color.parseColor("#304567") //Setting the size of the form in the legend
        barDataSet.formSize = 15f //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false) //setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f

        barChart.setOnChartValueSelectedListener(barChartOnChartValueSelectedListener())
    }

    private class barChartOnChartValueSelectedListener :
        OnChartValueSelectedListener {

        override fun onValueSelected(e: Entry?, h: Highlight?) {

        }

        override fun onNothingSelected() {}
    }

    private fun showBarChart() {
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "Title"


        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()

        initBarDataSet(barDataSet)
    }

    private fun connectSocket(currency: Currency) {
        SocketManager.connectToSocket(currency, object : SocketHandler {

            override fun onResponse(response: CurrencyResponse) {
                runOnUiThread {
                   valueList.add(response.data.price)
                    showBarChart()
                }
            }
            override fun onFailure(t: Throwable) {

            }
        })
    }


}