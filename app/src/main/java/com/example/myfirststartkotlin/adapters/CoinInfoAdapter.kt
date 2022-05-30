package com.example.myfirststartkotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirststartkotlin.R
import com.example.myfirststartkotlin.Utils.loadImage
import com.example.myfirststartkotlin.pojo.CoinPriceInfo
import kotlinx.android.synthetic.main.item_coin_info.view.*
import kotlinx.coroutines.delay as delay


class CoinInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    /// при присваивании нового значения будем обновлять список
    var coinInfoList: List<CoinPriceInfo> = listOf()
        set(value) {
            field = value

            notifyDataSetChanged()
        }
    var onCoinClickListener: OnCoinClickListener? = null


    interface OnCoinClickListener {
        fun onCoinClick(coin: CoinPriceInfo)
    }


    inner class CoinInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivLogoCoin = itemView.ivLogoCoin
        val tvSimbols = itemView.tvSimbols
        val tvPrice = itemView.tvPrice
        val tvLastUpdate = itemView.tvLastUpdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info, parent, false)
        return CoinInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder) {
            with(coin) {
                val symbolsTemlate = context.resources.getString(R.string.symbols_template)
                val lastUpdateTemlate = context.resources.getString(R.string.lastUpdate)
                tvSimbols.text = String.format(symbolsTemlate, fromsymbol, tosymbol)
                tvPrice.text = price.toString()
                tvLastUpdate.text = String.format(lastUpdateTemlate, getFormattedTime())
                ivLogoCoin.loadImage(getFullImgUrl())
                itemView.setOnClickListener {
                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }
    }


    override fun getItemCount(): Int = coinInfoList.size

}

