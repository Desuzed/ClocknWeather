package com.desuzed.clocknweather.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.mvvm.OnecallApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DailyAdapter(
    var list: ArrayList<OnecallApi.Daily>,
    val context: Context
) : RecyclerView.Adapter<DailyAdapter.CustomViewHolder>()  {
    var mOnecall: OnecallApi? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.daily_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(list[position], mOnecall!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateList (newList : ArrayList <OnecallApi.Daily>, onecall: OnecallApi){
        list = newList
        mOnecall = onecall
        notifyDataSetChanged()
    }
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dDate: TextView = itemView.findViewById(R.id.dDate)
        val dTemp: TextView = itemView.findViewById(R.id.dTemp)
        val dFeelsLike: TextView = itemView.findViewById(R.id.dFeelsLike)
        val dPressure: TextView = itemView.findViewById(R.id.dPressure)
        val dHumidity: TextView = itemView.findViewById(R.id.dHumidity)
        val dWindSpeed: TextView = itemView.findViewById(R.id.dWindSpeed)
        val dSunrise: TextView = itemView.findViewById(R.id.dSunrise)
        val dSunset: TextView = itemView.findViewById(R.id.dSunset)
//        val dMoonrise: TextView = itemView.findViewById(R.id.dMoonrise)
//        val dMoonset: TextView = itemView.findViewById(R.id.dMoonset)     Added recycler view fulfilled with hourly and daily forecast

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind (dailyItem : OnecallApi.Daily, onecall: OnecallApi){
            val sdfDate = SimpleDateFormat("dd/MM/yy")
            val sdfSun = SimpleDateFormat("HH:mm")
            var timezone = TimeZone.getTimeZone(onecall.timezone)
            sdfDate.timeZone = timezone
            sdfSun.timeZone = timezone
            dDate.text =  sdfDate.format(dailyItem.date*1000)
            dTemp.text = dailyItem.temp.toString()
            dFeelsLike.text = dailyItem.feelsLike.toString()
            dPressure.text = dailyItem.pressure.toString()
            dHumidity.text = dailyItem.humidity.toString()
            dWindSpeed.text = dailyItem.wind_speed.toString()
            dSunrise.text = sdfSun.format(dailyItem.sunrise*1000)
            dSunset.text = sdfSun.format(dailyItem.sunset*1000)
//            dMoonrise.text =  sdf.format(dailyItem.moonrise*1000)
//            dMoonset.text = sdf.format(dailyItem.moonset*1000)

        }
    }
}