package com.desuzed.everyweather.adapters

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
import com.desuzed.everyweather.R
import com.desuzed.everyweather.databinding.NextDayItemBinding
import com.desuzed.everyweather.mvvm.model.ForecastDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class NextDaysRvAdapter : RecyclerView.Adapter<NextDaysRvAdapter.NextDaysVH>() {
    private var list: ArrayList<ForecastDay> = ArrayList()
    private var mTimeZone: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDaysVH {
        return NextDaysVH(
            NextDayItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: NextDaysVH, position: Int) {
        val item = list[position]
        holder.clNextDays.setOnClickListener {
            val isExpanded = item.isExpanded
            item.isExpanded = !isExpanded
            notifyItemChanged(position)
        }
        holder.bind(list[position], mTimeZone!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: ArrayList<ForecastDay>, timeZone: String) {
        list = newList
        mTimeZone = timeZone
        notifyDataSetChanged()
    }

    class NextDaysVH(binding: NextDayItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvNextDate: TextView = binding.tvNextDate
        private val tvNextText: TextView = binding.tvNextText
        private val tvNextMaxTemp: TextView = binding.tvNextMaxTemp
        private val tvNextMinTemp: TextView = binding.tvNextMinTemp
        private val tvNextWind: TextView = binding.tvNextWind
        private val tvNextHumidity: TextView = binding.tvNextHumidity
        private val tvNextPressure: TextView = binding.tvNextPressure
        private val tvNextPop: TextView = binding.tvNextPop
        private val tvNextSun: TextView = binding.tvNextSun
        private val tvNextMoon: TextView = binding.tvNextMoon
        private val rvNextHourly: RecyclerView = binding.rvNextHourly
        private val nextIcon: ImageView = binding.nextIcon
        private val ivArrow: ImageView = binding.ivArrow
        private val expandableContainer: ConstraintLayout =
            binding.expandableContainer
        val clNextDays: ConstraintLayout = binding.clNextDays

        //TODO refactor  to mapper
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(forecastDay: ForecastDay, timeZone: String) {
            val sdf = SimpleDateFormat("dd.MM.yy")
            val day = forecastDay.day
            Glide
                .with(context)
                .load("https:${forecastDay.day.icon}")
                .into(nextIcon)
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            tvNextDate.text = sdf.format(forecastDay.dateEpoch * 1000)
            tvNextText.text = day.text
            tvNextMaxTemp.text = day.maxTemp.roundToInt().toString() + context.resources.getString(
                R.string.celsius
            )
            tvNextMinTemp.text = day.minTemp.roundToInt().toString() + context.resources.getString(
                R.string.celsius
            )
            tvNextWind.text = "${day.maxWind} km/h"
            tvNextPressure.text = "${forecastDay.hourForecast[0].pressureMb} mb"
            tvNextHumidity.text = "${day.avgHumidity}%"
            tvNextPop.text = "${day.popRain}%, ${day.totalPrecip} mm"
            tvNextSun.text = "${forecastDay.astro.sunrise}\n${forecastDay.astro.sunset}"
            tvNextMoon.text = "${forecastDay.astro.moonrise}\n${forecastDay.astro.moonset}"
            rvNextHourly.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val rvAdapter = HourAdapter()
            rvAdapter.updateList(forecastDay.hourForecast, timeZone)
            rvNextHourly.adapter = rvAdapter
            val isExpanded = forecastDay.isExpanded
            expandCollapse(isExpanded)

        }

        private fun expandCollapse(isExpanded: Boolean) {
            when (isExpanded) {
                true -> {
                    expandableContainer.visibility = View.VISIBLE
                    //  ivArrow.animate().rotation(0f).duration = 400
                    ivArrow.rotation = 0f
                }
                false -> {
                    expandableContainer.visibility = View.GONE
                    ivArrow.rotation = 180f
                    //  ivArrow.animate().rotation(180f).duration = 400
                }
            }
        }
    }
}