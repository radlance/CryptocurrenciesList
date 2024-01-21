package com.example.cryptocurrencieslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencieslist.R
import com.example.cryptocurrencieslist.databinding.CurrencyItemBinding
import com.example.cryptocurrencieslist.retrofit.CryptoCompareApi
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private val currencyList: MutableList<Currency> = ArrayList()

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CurrencyItemBinding.bind(itemView)
        fun bind(currency: Currency) = with(binding) {
            currencyTitleTv.text = buildString {
                append(currency.currencyName)
                append("/USD")
            }
            currentPriceTv.text = currency.price.toString()
            Picasso.get().load(currency.icon).into(currencyIconIv, object : Callback {
                override fun onSuccess() {
                    binding.loadImagePb.visibility = View.INVISIBLE
                }

                override fun onError(e: Exception?) {}
            })
            currentTimeTv.text = buildString {
                append("Время последнего обновления: ")
                append(currency.currentTime)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_item, parent, false)

        return CurrencyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(currencyList[position])
    }

    fun addCurrency(currency: Currency) {
        if (currencyList.none { it.currencyName == currency.currencyName }) {
            currencyList.add(currency)
            notifyDataSetChanged()
        }
    }

    fun updateData() {
        notifyDataSetChanged()
    }

    fun clearCurrencyList() {
        currencyList.clear()
    }
}