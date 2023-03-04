package com.example.cryptoapp.presentation.details_screen

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentDetailsBinding
import com.example.cryptoapp.domain.dto.CryptoDetailsResponse
import com.example.cryptoapp.presentation.base.BaseFragment
import com.example.cryptoapp.presentation.contracts.DetailsContract
import com.example.cryptoapp.presentation.item.CryptoItem
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailsBinding
        get() = FragmentDetailsBinding::inflate

    private val viewModel: DetailsViewModel by viewModel()
    var args: CryptoItem? = null
    var transitionName: String? = null
    private lateinit var lineChart: LineChart
    private var cryptoPrices = ArrayList<Float>()
    private lateinit var toolbar: Toolbar
    private var prevSelectedBtn: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val _args = requireArguments()
        args = DetailsFragmentArgs.fromBundle(_args).crypto
        transitionName = DetailsFragmentArgs.fromBundle(_args).transitionName

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.tvSelectedCryptoPrice.transitionName = transitionName
        binding.tvSelectedCryptoPrice.text = args?.price.plus(" $")
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun setup() {


        val crypto = arguments?.getParcelable<CryptoItem>("Crypto")

        toolbar = binding.detailsToolbar
//        toolbar.title = crypto?.title
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        context?.let {
            Glide.with(it)
                .load(args?.imageLink)
                .into(binding.ivDetailsLogo)
        }
        binding.tvDetailsTitle.text = args?.title
        binding.tvMarketCapPrice.text = args?.marketCap + " $"
        if (crypto?.priceChange?.get(0) != '-') {
            binding.tvSelectedCryptoPercent.text = "+${args?.priceChange} %"
        } else {
            binding.tvSelectedCryptoPercent.text = "${args?.priceChange} %"
            binding.tvSelectedCryptoPercent.setTextColor(R.color.color_red)
        }

        initObservers()
        initButtons()
        clickListener(binding.btn24h)

    }

    private fun initButtons() {
        val btnOneDay = binding.btn24h
        btnOneDay.setOnClickListener { clickListener(it) }
        val btnOneWeek = binding.btn1w
        btnOneWeek.setOnClickListener { clickListener(it) }
        val btnOneMonth = binding.btn1m
        btnOneMonth.setOnClickListener { clickListener(it) }
        val btnOneYear = binding.btn1y
        btnOneYear.setOnClickListener { clickListener(it) }
        val btnAll = binding.btnAll
        btnAll.setOnClickListener { clickListener(it) }
    }

    // Bicycle
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clickListener(view: View) {
        val crypto = arguments?.getParcelable<CryptoItem>("Crypto")
        when (view.id) {
            binding.btn24h.id -> {
                changeButtons(binding.btn24h)
                prevSelectedBtn = binding.btn24h
                getChart(args?.id.toString(), "1")
            }
            binding.btn1w.id -> {
                changeButtons(binding.btn1w)
                prevSelectedBtn = binding.btn1w
                getChart(args?.id.toString(), "7")
            }
            binding.btn1m.id -> {
                changeButtons(binding.btn1m)
                prevSelectedBtn = binding.btn1m
                getChart(args?.id.toString(), "30")
            }
            binding.btn1y.id -> {
                changeButtons(binding.btn1y)
                prevSelectedBtn = binding.btn1y
                getChart(args?.id.toString(), "365")
            }
            binding.btnAll.id -> {
                changeButtons(binding.btnAll)
                prevSelectedBtn = binding.btnAll
                getChart(args?.id.toString(), "max")
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeButtons(view: View) {
        if (prevSelectedBtn != null) {
            prevSelectedBtn!!.background = resources.getDrawable(R.color.color_transparent)
            prevSelectedBtn!!.setTextColor(resources.getColor(R.color.btn_inactive))
        }
        when (view.id) {
            binding.btn24h.id -> {
                binding.btn24h.background = resources.getDrawable(R.drawable.selected_button)
                binding.btn24h.setTextColor(resources.getColor(R.color.white))
            }
            binding.btn1w.id -> {
                binding.btn1w.background = resources.getDrawable(R.drawable.selected_button)
                binding.btn1w.setTextColor(resources.getColor(R.color.white))
            }
            binding.btn1m.id -> {
                binding.btn1m.background = resources.getDrawable(R.drawable.selected_button)
                binding.btn1m.setTextColor(resources.getColor(R.color.white))
            }
            binding.btn1y.id -> {
                binding.btn1y.background = resources.getDrawable(R.drawable.selected_button)
                binding.btn1y.setTextColor(resources.getColor(R.color.white))
            }
            binding.btnAll.id -> {
                binding.btnAll.background = resources.getDrawable(R.drawable.selected_button)
                binding.btnAll.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state.detailsCryptoState) {
                    is DetailsContract.DetailsCryptoState.Loading -> {

                    }
                    is DetailsContract.DetailsCryptoState.Success -> {

                    }
                }
            }
        }
    }

    private fun getChart(id: String, days: String) {

        binding.lineChart.visibility = View.INVISIBLE
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        var i = progressBar.progress

        cryptoPrices.clear()
        lifecycleScope.launch(Dispatchers.Default) {
            var response: CryptoDetailsResponse? = null
            runBlocking {
                response = viewModel.getDetailCrypto(id, days)
                while (response == null) {
                    i += 1
                    Runnable {
                        progressBar.progress = i
                    }
                    try {
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            progressBar.visibility = View.INVISIBLE

            response?.prices?.map {
                cryptoPrices.add(it[1])
            }

            withContext(Dispatchers.Main) {
                binding.lineChart.visibility = View.VISIBLE
                lineChart = binding.lineChart
                initLineChart()
                setDataToLineChart()
            }

        }

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

        for (i in cryptoPrices.indices) {
            val currentPrice = cryptoPrices[i]
            entries.add(Entry(i.toFloat(), currentPrice))
            val lineDataSet = LineDataSet(entries, "")
            lineDataSet.setDrawValues(false);
            lineDataSet.lineWidth = 2.7F;
            lineDataSet.color = resources.getColor(R.color.color_orange)
            lineDataSet.fillColor = resources.getColor(R.color.color_orange)
            lineDataSet.setDrawCircles(false)

            val data = LineData(lineDataSet)
            lineChart.data = data
            lineChart.invalidate()
        }
    }

}












