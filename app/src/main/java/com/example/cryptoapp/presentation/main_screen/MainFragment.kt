package com.example.cryptoapp.presentation.main_screen

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.util.TypedValue
import android.view.*
import androidx.annotation.ColorInt
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.presentation.base.BaseFragment
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.main_screen.util.OnClickListener
import com.example.cryptoapp.presentation.main_screen.util.PagingMainAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModel()

    private val cryptoItemListener = OnClickListener { crypto, view ->
        val direction = MainFragmentDirections.actionMainFragmentToDetailsFragment(
            crypto,
            view.transitionName
        )
        val extras = FragmentNavigator.Extras.Builder()
            .addSharedElement(view, view.transitionName)
            .build()
        findNavController().navigate(direction, extras)
    }

    private var adapter = PagingMainAdapter(cryptoItemListener)

    private lateinit var swipeLayout: SwipeRefreshLayout

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun setup() {

        initObservers()

        val toolbar = binding.mainToolbar
        toolbar.title = "Cryptocurrencies"

        val typedValue = TypedValue()
        val theme = context?.theme
        theme?.resolveAttribute(R.attr.colorOnBackground, typedValue, true)
        @ColorInt val colorWhite = typedValue.data

        toolbar.setTitleTextColor(colorWhite)
        toolbar.inflateMenu(R.menu.toolbar_main)
        toolbar.setOnMenuItemClickListener {
            viewModel.setEvent(MainContract.Event.OnSortClicked)
            true
        }

        binding.rvCrypto.layoutManager = LinearLayoutManager(
            binding.root.context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvCrypto.adapter = adapter

        swipeLayout = binding.swipeContainer
        swipeLayout.setOnRefreshListener {
            viewModel.setEvent(MainContract.Event.OnUpdateScroll)
        }

        theme?.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true)
        @ColorInt val colorGreen = typedValue.data

        swipeLayout.setColorSchemeColors(colorGreen)

    }

    private fun initObservers() {

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state.cryptoState) {
                    is MainContract.CryptoState.Default -> {
                        lifecycleScope.launch {
                            viewModel.getCryptosFromDbPagingData().collectLatest {
                                adapter.submitData(it)
                            }
                        }
                    }
                    is MainContract.CryptoState.OrderedByPrice -> {
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.getCryptosByPricePagingData().collectLatest {
                                adapter.submitData(it)
                            }
                        }
                    }
                    is MainContract.CryptoState.OrderedAlphabetically -> {
                        lifecycleScope.launch {
                            viewModel.getCryptosAlphabeticallyPagingData().collectLatest {
                                adapter.submitData(it)
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect {
                when (it) {
                    is MainContract.Effect.ShowDialogFragment -> showSortDialog()
                    is MainContract.Effect.Update -> refresh()
                    is MainContract.Effect.UpdateRecycler -> updateRecycler()
                }
            }
        }

    }

    private fun updateRecycler() {
        swipeLayout.isRefreshing = false
        lifecycleScope.launch {
            viewModel.getCryptosPagingData().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun refresh() {
        viewModel.refreshCryptos()
    }

    private fun showSortDialog(): Boolean {
        var sort = "By price"
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Sort")
        val items = arrayOf("By price", "Alphabetically")
        val checkedItem = 0
        alertDialog.setSingleChoiceItems(items, checkedItem) { _, which ->
            when (which) {
                0 -> {
                    sort = "By price"
                }
                1 -> {
                    sort = "Alphabetically"
                }
            }
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        alertDialog.setPositiveButton("Ok") { dialog, _ ->
            dialog.cancel()
            if (sort == "By price") {
                viewModel.setState { copy(cryptoState = MainContract.CryptoState.OrderedByPrice) }
            }
            if (sort == "Alphabetically") {
                viewModel.setState { copy(cryptoState = MainContract.CryptoState.OrderedAlphabetically) }
            }
        }
        val show = alertDialog.show()
        show.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GREEN)
        show.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        return true
    }

}




















