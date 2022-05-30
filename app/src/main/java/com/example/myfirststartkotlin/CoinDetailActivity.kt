package com.example.myfirststartkotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myfirststartkotlin.Utils.loadImage
import com.example.myfirststartkotlin.pojo.CoinPriceInfo
import kotlinx.android.synthetic.main.activity_coin_detail.*

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)


        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)){
            finish()
            return
        }
        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)

        viewModel = ViewModelProvider(this)[CoinViewModel::class.java]
        viewModel.getDetailInfo(fromSymbol!!).observe(this, Observer {
            tvPrice.text = it.price.toString()
            tvMinPrice.text = it.lowday.toString()
            tvMaxPrice.text = it.highday.toString()
            tvLastSdelka.text = it.lastmarket
            tvLastUpdate.text = it.getFormattedTime()
            tvFromSymbol.text = it.fromsymbol
            tvToSymbol.text = it.tosymbol
            ivLogoCoin.loadImage(it.getFullImgUrl())


        })
    }

    companion object{
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol:String):Intent{
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}