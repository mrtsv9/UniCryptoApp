package com.example.cryptoapp.presentation.details_screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentDetailsBinding
import com.example.cryptoapp.domain.dto.CryptoDetailsResponse
import com.example.cryptoapp.domain.dto.CryptoPrice
import com.example.cryptoapp.presentation.base.BaseFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailsBinding
        get() = FragmentDetailsBinding::inflate

    private lateinit var lineChart: LineChart
    private var cryptoPrices = ArrayList<CryptoDetailsResponse>()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun setup() {

        Toast.makeText(context, "qwe", Toast.LENGTH_SHORT).show()

        val toolbar = binding.detailsToolbar
        toolbar.title = "Bitcoin"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        toolbar.logo = resources.getDrawable(R.drawable.ic_bitcoin24)

        lineChart = binding.lineChart

        initLineChart()
        setDataToLineChart()

    }

    private fun initLineChart() {
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        lineChart.axisRight.isEnabled = false

        //remove legend
        lineChart.legend.isEnabled = false

        //remove description label
        lineChart.description.isEnabled = false
    }

    private fun setDataToLineChart() {

        val entries: ArrayList<Entry> = ArrayList()
        cryptoPrices.add(CryptoDetailsResponse(CryptoPrice(1.0F)))
        cryptoPrices.add(CryptoDetailsResponse(CryptoPrice(4.0F)))
        cryptoPrices.add(CryptoDetailsResponse(CryptoPrice(-2.0F)))
        cryptoPrices.add(CryptoDetailsResponse(CryptoPrice(0.0F)))
        cryptoPrices.add(CryptoDetailsResponse(CryptoPrice(6.0F)))
        cryptoPrices.add(CryptoDetailsResponse(CryptoPrice(14.0F)))

        for (i in cryptoPrices.indices) {
            val currentPrice = cryptoPrices[i]
            entries.add(Entry(i.toFloat(), currentPrice.cryptoPrice.price))
            val lineDataSet = LineDataSet(entries, "")
            val data = LineData(lineDataSet)
            lineChart.data = data

            lineChart.invalidate()
        }
    }

}