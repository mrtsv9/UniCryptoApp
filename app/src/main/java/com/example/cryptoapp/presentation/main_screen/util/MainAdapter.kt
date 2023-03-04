package com.example.cryptoapp.presentation.main_screen.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.presentation.item.CryptoItem
import com.example.cryptoapp.presentation.main_screen.MainFragmentDirections

//import com.example.cryptoapp.data.models.Crypto
//import com.example.cryptoapp.domain.dto.CryptoDto

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var listOfCrypto = emptyList<CryptoItem>().toMutableList()

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivLogo: ImageView = itemView.findViewById(R.id.iv_crypto_logo)
        private val tvAbbr: TextView = itemView.findViewById(R.id.tv_crypto_abbr)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_crypto_title)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_crypto_price)

        @SuppressLint("SetTextI18n")
        fun bind(crypto: CryptoItem) {

            Glide.with(itemView)
                .load(crypto.imageLink)
                .placeholder(R.drawable.ic_bitcoin48)
                .into(ivLogo)
            tvAbbr.text = crypto.abbr
            tvTitle.text = crypto.title
            tvPrice.text = "${crypto.price} $"

//            itemView.setOnClickListener {
//                tvPrice.transitionName = "transition$position"
//
//                val transitionName = ViewCompat.getTransitionName(itemView)
//                val extras = FragmentNavigator.Extras.Builder()
//                    .addSharedElement(tvPrice, transitionName.toString())
//                    .build()
//                val direction = transitionName?.let {  transition ->
//                    MainFragmentDirections.actionMainFragmentToDetailsFragment(
//                        crypto,
//                        transition
//                    )
//                }
//                direction?.let { it1 -> Navigation.findNavController(itemView).navigate(it1, extras) }
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_crypto, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(listOfCrypto[position])
    }

    override fun getItemCount(): Int {
        return listOfCrypto.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(cryptos: List<CryptoItem>?) {
        if (cryptos != null) {
            this.listOfCrypto.addAll(cryptos)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(cryptos: List<CryptoItem>?) {
        if (cryptos != null) {
            this.listOfCrypto.addAll(cryptos)
        }
        notifyDataSetChanged()
    }
}