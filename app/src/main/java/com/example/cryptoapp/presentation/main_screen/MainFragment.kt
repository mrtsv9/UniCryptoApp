package com.example.cryptoapp.presentation.main_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.presentation.base.BaseFragment
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.details_screen.DetailsFragment
import com.example.cryptoapp.presentation.item.CryptoItem
import com.example.cryptoapp.presentation.main_screen.util.OnClickListener
import com.example.cryptoapp.presentation.main_screen.util.PagingMainAdapter
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text
import java.nio.file.Files.find

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModel()

    private val cryptoItemListener = OnClickListener { crypto, view ->
        val direction = MainFragmentDirections.actionMainFragmentToDetailsFragment(crypto,
            view.transitionName)
//        val extras = FragmentNavigatorExtras(view to crypto.price)
        val extras = FragmentNavigator.Extras.Builder()
            .addSharedElement(view, view.transitionName)
            .build()
        findNavController().navigate(direction, extras)
    }

    private var adapter = PagingMainAdapter(cryptoItemListener)

    private lateinit var swipeLayout: SwipeRefreshLayout
    private var extras: FragmentNavigator.Extras? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun setup() {
        val toolbar = binding.mainToolbar
        toolbar.title = "Cryptocurrencies"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.inflateMenu(R.menu.toolbar_main)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_alphabetically -> {
                    lifecycleScope.launch {
                        viewModel.fetchCryptosAlphabeticallyByPage().collectLatest {
                            adapter.submitData(it)
                        }
                    }
                }

                R.id.sort_price -> {
                    lifecycleScope.launch {
                        viewModel.fetchCryptosByPriceByPage().collectLatest {
                            adapter.submitData(it)
                        }
                    }
                }
            }
            true
        }

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
                when (state.cryptoState) {
//                    is MainContract.CryptoState.Loading -> {}
                    is MainContract.CryptoState.Error -> {
//                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                    is MainContract.CryptoState.Success -> {
                        binding.rvCrypto.layoutManager = LinearLayoutManager(
                            binding.root.context,
                            LinearLayoutManager.VERTICAL, false
                        )
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
//
//    private fun transition(view: View) {
////        extras = FragmentNavigatorExtras(view to "shared_element_container")
//        extras = FragmentNavigatorExtras(view to view.transitionName)
//        val fragmentB = DetailsFragment()
//        fragmentB.sharedElementEnterTransition = MaterialContainerTransform()
//    }

//    private fun cryptoClickListener(crypto: CryptoItem) {
//
//        val bundle = Bundle()
//        bundle.putParcelable("Crypto", crypto)
////        val text = (activity as? MainActivity)!!.findViewById<TextView>(R.id.tv_crypto_price)
////        val extras = FragmentNavigatorExtras(text to "price")
//
//        findNavController().navigate(
//            R.id.action_mainFragment_to_detailsFragment,
//            bundle,
//            null,
//            extras
//        )
//    }

}