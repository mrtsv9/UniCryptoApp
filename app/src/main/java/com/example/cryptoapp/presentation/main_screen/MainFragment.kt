package com.example.cryptoapp.presentation.main_screen

import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.presentation.base.BaseFragment
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.item.CryptoItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun setup() {
        initObservers()
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
                        val adapter = MainAdapter()
                        binding.rvCrypto.layoutManager = LinearLayoutManager(binding.root.context,
                            LinearLayoutManager.VERTICAL, false)
                        binding.rvCrypto.adapter = adapter

                        val db = CryptoDatabase.getDatabase(requireContext())
                        val cryptos = viewModel.getCryptosFromDb(db)

//                        Log.d("KEK", "Putting data into adapter")
                        adapter.setData(cryptos.map {
                            CryptoItem(it.abbr, it.title, it.imageLink, it.price)
                        })
                    }
                }
            }
        }

    }

}