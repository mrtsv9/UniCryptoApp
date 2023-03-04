package com.example.cryptoapp.presentation.main_screen.util

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.presentation.details_screen.DetailsFragment
import com.example.cryptoapp.presentation.item.CryptoItem
import com.example.cryptoapp.presentation.main_screen.MainFragment
import kotlinx.coroutines.withContext
import java.math.RoundingMode

class PagingMainAdapter(
    private val onClickListener: OnClickListener
) : PagingDataAdapter<CryptoItem, PagingMainAdapter.PagingMainViewHolder>(CryptoDiffCallback()) {

    class CryptoDiffCallback : DiffUtil.ItemCallback<CryptoItem>() {

        override fun areItemsTheSame(oldItem: CryptoItem, newItem: CryptoItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CryptoItem, newItem: CryptoItem): Boolean {
            return oldItem.abbr == newItem.abbr && oldItem.imageLink == newItem.imageLink
                    && oldItem.price == newItem.price && oldItem.title == newItem.title
        }

    }

    class PagingMainViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivLogo: ImageView = itemView.findViewById(R.id.iv_crypto_logo)
        private val tvAbbr: TextView = itemView.findViewById(R.id.tv_crypto_abbr)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_crypto_title)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_crypto_price)

        @SuppressLint("SetTextI18n")
        fun bind(crypto: CryptoItem, onClickListener: OnClickListener) {
            itemView.setOnClickListener {
                tvPrice.transitionName = "transition$position"
                onClickListener.onClick(crypto, tvPrice)
            }

            Glide.with(itemView)
                .load(crypto.imageLink)
                .placeholder(R.drawable.ic_bitcoin48)
                .into(ivLogo)
            tvAbbr.text = crypto.abbr
            tvTitle.text = crypto.title
            if (crypto.price!!.toFloatOrNull()!! >= 1) {
                tvPrice.text = "${crypto.price} $"
            } else {
                val decimal = crypto.price.toBigDecimal().setScale(5, RoundingMode.HALF_EVEN)
                tvPrice.text = decimal.toString().plus(" $")
            }

        }

    }

    override fun onBindViewHolder(holder: PagingMainViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onClickListener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingMainViewHolder {
        return PagingMainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_crypto, parent, false)
        )
    }

}

class OnClickListener(val clickListener: (CryptoItem, TextView) -> Unit) {
    fun onClick(
        crypto: CryptoItem,
        view: TextView
    ) = clickListener(crypto, view)
}


