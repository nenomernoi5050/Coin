package com.example.myfirststartkotlin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myfirststartkotlin.api.ApiFactory
import com.example.myfirststartkotlin.database.AppDataBase
import com.example.myfirststartkotlin.pojo.CoinPriceInfo
import com.example.myfirststartkotlin.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class CoinViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDataBase.getInstance(app)
    private val compositeDisposable = CompositeDisposable()
    val priceList = db.coinPriceInfoDao().getPriceList()

    init {
        loadData()
    }


    fun getDetailInfo(fromSymbol:String):LiveData<CoinPriceInfo>{
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fromSymbol = fromSymbol)
    }


    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 20)
            .map { it.data?.map { it?.coinInfo?.name }?.joinToString(",") }
            .flatMap { ApiFactory.apiService.getFullPriceList(fromSyms = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(30, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                Log.d("TEST_OF_LOG", it.toString())
            }, {
                Log.d("TEST_OF_LOG", it.message.toString())

            })
        compositeDisposable.add(disposable)

    }


    private fun getPriceListFromRawData(coinPriceInfoRawData: CoinPriceInfoRawData): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObj ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currentJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currentJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currentJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}