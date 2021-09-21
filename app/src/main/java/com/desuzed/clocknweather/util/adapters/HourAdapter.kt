package com.desuzed.clocknweather.util.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.retrofit.pojo.Hour
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class HourAdapter(
    val context: Context
) : RecyclerView.Adapter<HourAdapter.HourVH>() {
    private var list: ArrayList<Hour> = ArrayList()
    private var mTimeZone : String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourVH {
        val view = LayoutInflater.from(context).inflate(R.layout.hour_item, parent, false)
        return HourVH(view, context)
    }

    override fun onBindViewHolder(holder: HourVH, position: Int) {
        holder.bind(list[position], mTimeZone!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateList (newList : ArrayList <Hour>, timeZone: String){
        list = newList
        mTimeZone = timeZone
        notifyDataSetChanged()
    }

    class HourVH (itemView: View, val context : Context) :RecyclerView.ViewHolder(itemView) {
        private val hTime: TextView = itemView.findViewById(R.id.hTime)
        private val hTempC: TextView = itemView.findViewById(R.id.hTempC)
        private val hWind: TextView = itemView.findViewById(R.id.hWind)
        private val hIcon: ImageView = itemView.findViewById(R.id.hIcon)
        private val hWindDegree: ImageView = itemView.findViewById(R.id.hWindDegree)
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(hour: Hour, timeZone: String) {
            val sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            hTime.text =  sdf.format(hour.timeEpoch*1000)
            hTempC.text = hour.temp.roundToInt().toString() + context.resources.getString(R.string.celsius)
            hWind.text = "${hour.windSpeed.toInt()} km/h"
            hWindDegree.rotation = hour.windDegree.toFloat() - 180
            Glide
                .with(context)
                .load("https:${hour.condition?.icon}")

                .into(hIcon)
        }

    }
}
