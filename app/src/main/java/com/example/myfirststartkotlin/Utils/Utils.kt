package com.example.myfirststartkotlin.Utils

import android.app.Activity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.*
import com.example.myfirststartkotlin.R
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


fun convertTimestampToTime(
    timestamp:Long?
):String{
    if (timestamp  == null) return ""
    val stamp = Timestamp(timestamp * 1000)
    val date = Date(stamp.time)
    val pattern = "HH:mm:ss"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}


fun ImageView.loadImage(image: String?) {
    ifNotDestroyed { Glide.with(this).load(image).placeholder(R.drawable.placeholder_feed).error(
        R.drawable.placeholder_feed).centerCrop().into(this) }

}

private fun View.ifNotDestroyed(block: () -> Unit){
    if (!(context as Activity).isDestroyed) { block() }
}