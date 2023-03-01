package com.example.cryptoapp.presentation.main_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.presentation.base.BaseFragment
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.main_screen.util.PagingMainAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModel()
    private val adapter = PagingMainAdapter()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun setup() {
        initObservers()
        collectUiState()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state.cryptoState) {
                    is MainContract.CryptoState.Loading -> {
                        //
                    }
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
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.fetchCryptosByPage().collectLatest {
                adapter.submitData(it)
            }
        }
    }

}