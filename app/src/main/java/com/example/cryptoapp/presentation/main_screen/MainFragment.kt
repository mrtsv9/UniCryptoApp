package com.example.cryptoapp.presentation.main_screen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.request.Disposable
import com.example.cryptoapp.R
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.presentation.base.BaseFragment
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.main_screen.util.OnClickListener
import com.example.cryptoapp.presentation.main_screen.util.PagingMainAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
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

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun setup() {
        val toolbar = binding.mainToolbar
        toolbar.title = "Cryptocurrencies"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
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

        lifecycleScope.launch {
            viewModel.fetchCryptosByPageFromDb().collectLatest {
                adapter.submitData(it)
            }
        }

        initObservers()

        swipeLayout = binding.swipeContainer
        swipeLayout.setOnRefreshListener {
            viewModel.setEvent(MainContract.Event.OnUpdateScroll)
        }
        swipeLayout.setColorSchemeColors(
            resources.getColor(R.color.colorPrimaryVariant)
        )

    }

    private fun initObservers() {
        lifecycleScope.launch(Dispatchers.Unconfined) {
            viewModel.uiState.collect { state ->
                when (state.cryptoState) {
                    is MainContract.CryptoState.Default -> {
                        lifecycleScope.launch {
                            viewModel.fetchCryptosByPage().collectLatest {
                                adapter.submitData(it)
                            }
                        }
                    }
                    is MainContract.CryptoState.OrderedByPrice -> {
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.fetchCryptosByPriceByPage().collectLatest {
                                adapter.submitData(it)
                            }
                        }
                        swipeLayout.isRefreshing = false

                    }
                    is MainContract.CryptoState.OrderedAlphabetically -> {
                        lifecycleScope.launch {
                            viewModel.fetchCryptosAlphabeticallyByPage().collectLatest {
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
                    is MainContract.Effect.UpdateRecycler -> refresh()
                }
            }
        }

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
                viewModel.setState { copy(MainContract.CryptoState.OrderedByPrice) }
            }
            if (sort == "Alphabetically") {
                viewModel.setState { copy(MainContract.CryptoState.OrderedAlphabetically) }
            }
        }
        val show = alertDialog.show()
        show.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GREEN)
        show.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        return true
    }

    private fun refresh() {
        val db = context?.let { CryptoDatabase.getDatabase(it) }
        if (db != null) {
            viewModel.insertCryptos(db)
        }

        lifecycleScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Default) {
                viewModel.setState { copy(MainContract.CryptoState.Default) }
                viewModel.fetchCryptosByPage().collectLatest {
                    adapter.submitData(it)
                }
            }
        }
        swipeLayout.isRefreshing = false
    }

}




















