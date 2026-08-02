package com.example.cryptoapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCoinInfoBinding
import com.example.cryptoapp.domain.entities.CoinInfo
import com.squareup.picasso.Picasso

class CoinInfoListAdapter : ListAdapter<CoinInfo, CoinInfoViewHolder>(
    CoinInfoDiffCallback()
) {
    var onCoinClickListener: ((CoinInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val binding = ItemCoinInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CoinInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = getItem(position)
        val binding = holder.binding
        val context = holder.itemView.context
        with(holder.binding) {
            val symbolsTemplate = context.resources.getString(R.string.symbols_template)
            val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
            binding.tvSymbols.text = String.format(symbolsTemplate, coin.fromSymbol, coin.toSymbol)
            binding.tvPrice.text = coin.price
            binding.tvLastUpdate.text = String.format(lastUpdateTemplate, coin.lastUpdate)
            Picasso.get().load(coin.imageUrl).into(binding.ivLogoCoin)
            root.setOnClickListener {
                onCoinClickListener?.invoke(coin)
            }
        }
    }
}