package com.example.cryptoapp.presentation.main_screen

import android.annotation.SuppressLint
import android.view.*
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptoapp.R
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.presentation.base.BaseFragment
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.details_screen.DetailsFragment
import com.example.cryptoapp.presentation.item.CryptoItem
import com.example.cryptoapp.presentation.main_screen.util.PagingMainAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModel()
    private val adapter = PagingMainAdapter() { cryptoClickListener(it) }
    private lateinit var swipeLayout: SwipeRefreshLayout

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun setup() {

        val toolbar = binding.mainToolbar
        toolbar.title = "Cryptocurrencies"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.inflateMenu(R.menu.toolbar_main)

        initObservers()
        collectUiState()

        swipeLayout = binding.swipeContainer
        swipeLayout.setOnRefreshListener {
            refresh()
        }
        swipeLayout.setColorSchemeColors(
            resources.getColor(R.color.colorPrimaryVariant)
        )
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state.cryptoState) {
//                    is MainContract.CryptoState.Loading -> {}
                    is MainContract.CryptoState.Error -> {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                    is MainContract.CryptoState.Success -> {
                        binding.rvCrypto.layoutManager = LinearLayoutManager(binding.root.context,
                            LinearLayoutManager.VERTICAL, false)
                        binding.rvCrypto.adapter = adapter

//                        lifecycleScope.launch {
//                            viewModel.fetchCryptosByPageFromDb().collectLatest {
//                                adapter.submitData(it)
//                            }
//                        }
                    }
                }
            }
        }

//        lifecycleScope.launch {
//            viewModel.effect.collect {
//                when(it) {
//                    is MainContract.Effect.InternetError -> {
//                        Toast.makeText(context, "NO INTERNET", Toast.LENGTH_SHORT).show()
//                    }
//                    is MainContract.Effect.InternetSuccess -> {
//                        Toast.makeText(context, "SUCCESSFULLY CONNECTED", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.fetchCryptosByPage().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun refresh() {
        lifecycleScope.launch {
            viewModel.fetchCryptosByPage().collectLatest {
                adapter.submitData(it)
             }
        }
        swipeLayout.isRefreshing = false

        val db = context?.let { CryptoDatabase.getDatabase(it) }
        if (db != null) {
            viewModel.insertCryptos(db)
        }
    }

    private fun cryptoClickListener(crypto: CryptoItem) {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment())
    }

}