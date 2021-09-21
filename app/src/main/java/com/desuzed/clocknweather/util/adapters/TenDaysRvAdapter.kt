package com.desuzed.clocknweather.util.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.retrofit.pojo.ForecastDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class TenDaysRvAdapter(
    val context: Context,
) : RecyclerView.Adapter<TenDaysRvAdapter.TenDaysVH>() {
    private var list: ArrayList<ForecastDay> = ArrayList()
    private var mTimeZone: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenDaysVH {
        val view = LayoutInflater.from(context).inflate(R.layout.ten_day_item, parent, false)
        return TenDaysVH(view, context)
    }

    override fun onBindViewHolder(holder: TenDaysVH, position: Int) {
        holder.bind(list[position], mTimeZone!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList (newList : ArrayList <ForecastDay>, timeZone: String){
        list = newList
        mTimeZone = timeZone
        notifyDataSetChanged()
    }


    class TenDaysVH(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val tvTenDate: TextView = itemView.findViewById(R.id.tvTenDate)
        private val tvTenText: TextView = itemView.findViewById(R.id.tvTenText)
        private val tvTenMaxTemp: TextView = itemView.findViewById(R.id.tvTenMaxTemp)
        private val tvTenMinTemp: TextView = itemView.findViewById(R.id.tvTenMinTemp)
        private val tvWindTen: TextView = itemView.findViewById(R.id.tvWindTen)
        private val tvHumidityTen: TextView = itemView.findViewById(R.id.tvHumidityTen)
        private val tvPressureTen: TextView = itemView.findViewById(R.id.tvPressureTen)
        private val tvPopTen: TextView = itemView.findViewById(R.id.tvPopTen)
        private val tvSunTen: TextView = itemView.findViewById(R.id.tvSunTen)
        private val tvMoonTen: TextView = itemView.findViewById(R.id.tvMoonTen)
        private val rvTenHourly: RecyclerView = itemView.findViewById(R.id.rvTenHourly)
        private val tenIcon: ImageView = itemView.findViewById(R.id.tenIcon)
        private val clDetailedWeather: ConstraintLayout = itemView.findViewById(R.id.containerDetailedWeather)
        private val clTenDays: ConstraintLayout = itemView.findViewById(R.id.clTenDays)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(forecastDay: ForecastDay, timeZone: String) {
            val sdf = SimpleDateFormat("dd.MM.yy")
            val day = forecastDay.day
            Glide
                .with(context)
                .load("https:${forecastDay.day?.condition?.icon}")
                .into(tenIcon)
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            tvTenDate.text =  sdf.format(forecastDay.dateEpoch*1000)
            tvTenText.text = day?.condition?.text.toString()
            tvTenMaxTemp.text = day?.maxTemp?.roundToInt().toString() + context.resources.getString(R.string.celsius)
            tvTenMinTemp.text = day?.minTemp?.roundToInt().toString() + context.resources.getString(R.string.celsius)
            tvWindTen.text = "${day?.maxWind} km/h"
            tvPressureTen.text = "${forecastDay.hour!![0].pressureMb} mb"
            tvHumidityTen.text = "${day?.avgHumidity.toString()}%"
            tvPopTen.text = "${day?.popRain.toString()}%, ${day?.totalPrecip.toString()} mm"
            tvSunTen.text = "${forecastDay.astro?.sunrise}\n${forecastDay.astro?.sunset}"
            tvMoonTen.text = "${forecastDay.astro?.moonrise}\n${forecastDay.astro?.moonset}"
            rvTenHourly.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,  false)
            val rvAdapter = HourAdapter(context)
            rvAdapter.updateList(forecastDay.hour!!, timeZone)
            rvTenHourly.adapter = rvAdapter
            clTenDays.setOnClickListener{
                when (clDetailedWeather.visibility) {
                    View.GONE -> {
                        clDetailedWeather.visibility = View.VISIBLE
                    }
                    View.VISIBLE -> clDetailedWeather.visibility = View.GONE
                }
            }
        }

    }
}