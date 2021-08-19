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

class HourlyAdapter(
    var list: ArrayList<OnecallApi.Hourly>,
    val context: Context
) : RecyclerView.Adapter<HourlyAdapter.CustomViewHolder>() {
    var mOnecall: OnecallApi? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hourly_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(list[position], mOnecall!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList (newList : ArrayList <OnecallApi.Hourly>, onecall: OnecallApi){
        list = newList
        mOnecall = onecall

        notifyDataSetChanged()
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hDate: TextView = itemView.findViewById(R.id.hDate)
        val hTemp: TextView = itemView.findViewById(R.id.hTemp)
        val hFeelsLike: TextView = itemView.findViewById(R.id.hFeelsLike)
        val hPressure: TextView = itemView.findViewById(R.id.hPressure)
        val hHumidity: TextView = itemView.findViewById(R.id.hHumidity)
        val hWindSpeed: TextView = itemView.findViewById(R.id.hWindSpeed)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind (hourlyItem : OnecallApi.Hourly, onecall: OnecallApi){
            val sdf = SimpleDateFormat("dd/MM/yy; \nHH:mm")
            sdf.timeZone = TimeZone.getTimeZone(onecall.timezone)
            hDate.text =  sdf.format(hourlyItem.date*1000)
            hTemp.text = hourlyItem.temp.toString()
            hFeelsLike.text = hourlyItem.feels_like.toString()
            hPressure.text = hourlyItem.pressure.toString()
            hHumidity.text = hourlyItem.humidity.toString()
            hWindSpeed.text = hourlyItem.wind_speed.toString()
        }
    }
}