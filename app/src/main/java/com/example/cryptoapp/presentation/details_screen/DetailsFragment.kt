package com.example.cryptoapp.presentation.details_screen

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
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
import kotlinx.coroutines.*
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

        val _args: Bundle = requireArguments()
        args = DetailsFragmentArgs.fromBundle(_args).crypto
        transitionName = DetailsFragmentArgs.fromBundle(_args).transitionName

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.tvSelectedCryptoPrice.transitionName = transitionName
        binding.tvSelectedCryptoPrice.text = args?.price.plus(" $ ")
        return binding.root
    }

    override fun setup() {

        toolbar = binding.detailsToolbar

        val typedValue = TypedValue()
        val theme = context?.theme
        theme?.resolveAttribute(R.attr.colorOnBackground, typedValue, true)
        @ColorInt val colorWhite = typedValue.data

        toolbar.setTitleTextColor(colorWhite)
        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back, null)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        context?.let {
            Glide.with(it)
                .load(args?.imageLink)
                .into(binding.ivDetailsLogo)
        }
        binding.tvDetailsTitle.text = args?.title
        binding.tvMarketCapPrice.text = args?.marketCap?.dropLast(6).plus(" B")

        @SuppressLint("SetTextI18n")
        if (args?.priceChange?.get(0) != '-') {
            binding.tvSelectedCryptoPercent.text = "+${args?.priceChange} %"
        } else {
            binding.tvSelectedCryptoPercent.text = "${args?.priceChange} %"
            binding.tvSelectedCryptoPercent.setTextColor(Color.RED)
        }

        initObservers()
        initButtons()
        clickListener(binding.btn24h)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
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

    private fun clickListener(view: View) {
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

    private fun changeButtons(view: View) {

        val typedValue = TypedValue()
        val theme = context?.theme
        theme?.resolveAttribute(R.attr.colorButtonNormal, typedValue, true)
        @ColorInt val color = typedValue.data
        theme?.resolveAttribute(R.attr.colorOnBackground, typedValue, true)
        @ColorInt val colorWhite = typedValue.data

        if (prevSelectedBtn != null) {
            prevSelectedBtn!!.background =
                ResourcesCompat.getDrawable(resources, R.color.color_transparent, null)
            prevSelectedBtn!!.setTextColor(color)
        }
        when (view.id) {
            binding.btn24h.id -> {
                binding.btn24h.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.selected_button, null)
                binding.btn24h.setTextColor(colorWhite)
            }
            binding.btn1w.id -> {
                binding.btn1w.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.selected_button, null)
                binding.btn1w.setTextColor(colorWhite)
            }
            binding.btn1m.id -> {
                binding.btn1m.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.selected_button, null)
                binding.btn1m.setTextColor(colorWhite)
            }
            binding.btn1y.id -> {
                binding.btn1y.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.selected_button, null)
                binding.btn1y.setTextColor(colorWhite)
            }
            binding.btnAll.id -> {
                binding.btnAll.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.selected_button, null)
                binding.btnAll.setTextColor(colorWhite)
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state.detailsCryptoState) {
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
            val response: CryptoDetailsResponse? = viewModel.getDetailCrypto(id, days)
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
            progressBar.visibility = View.INVISIBLE

            response.prices.map {
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

        lineChart.legend.isEnabled = false

        lineChart.description.isEnabled = false
    }

    private fun setDataToLineChart() {

        val entries: ArrayList<Entry> = ArrayList()

        for (i in cryptoPrices.indices) {

            val typedValue = TypedValue()
            val theme = context?.theme
            theme?.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true)
            @ColorInt val color = typedValue.data

            val currentPrice = cryptoPrices[i]
            entries.add(Entry(i.toFloat(), currentPrice))
            val lineDataSet = LineDataSet(entries, "")
            lineDataSet.setDrawValues(false)
            lineDataSet.lineWidth = 2.7F
            lineDataSet.color = color
            lineDataSet.setDrawCircles(false)

            val data = LineData(lineDataSet)
            lineChart.data = data
            lineChart.invalidate()
        }
    }

}












