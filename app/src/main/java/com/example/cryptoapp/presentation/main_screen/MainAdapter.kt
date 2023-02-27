package com.example.cryptoapp.presentation.main_screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.presentation.item.CryptoItem

//import com.example.cryptoapp.data.models.Crypto
//import com.example.cryptoapp.domain.dto.CryptoDto

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

//    private var listOfCrypto = emptyList<Crypto>()
//    private var listOfCrypto = emptyList<CryptoDto>()
//    private var listOfCrypto = emptyList<CryptoEntity>()
    private var listOfCrypto = emptyList<CryptoItem>()

    class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val ivLogo: ImageView = itemView.findViewById(R.id.iv_crypto_logo)
        private val tvAbbr: TextView = itemView.findViewById(R.id.tv_crypto_abbr)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_crypto_title)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_crypto_price)

        //        fun bind(crypto: Crypto) {
        @SuppressLint("SetTextI18n")
        //fun bind(crypto: CryptoDto) {
        //fun bind(crypto: CryptoEntity) {
        fun bind(crypto: CryptoItem) {
                    Glide.with(itemView)
                        .load(crypto.imageLink)
                        .placeholder(R.drawable.ic_bitcoin)
                        .into(ivLogo)
                    tvAbbr.text = crypto.abbr
                    tvTitle.text = crypto.title
                    tvPrice.text = "${crypto.price} $"
                }

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_crypto, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(listOfCrypto[position])
    }

    override fun getItemCount(): Int {
        return listOfCrypto.size
    }

    @SuppressLint("NotifyDataSetChanged")
//    fun setData(cryptos: List<Crypto>?) {
//    fun setData(cryptos: List<CryptoDto>?) {
//    fun setData(cryptos: List<CryptoEntity>?) {
    fun setData(cryptos: List<CryptoItem>?) {
        if(cryptos != null) {
        this.listOfCrypto = cryptos
        }
        notifyDataSetChanged()
    }
}