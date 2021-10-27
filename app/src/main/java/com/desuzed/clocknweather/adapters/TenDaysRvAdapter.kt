package com.desuzed.clocknweather.adapters

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
import com.desuzed.clocknweather.databinding.TenDayItemBinding
import com.desuzed.clocknweather.network.dto.ForecastDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class TenDaysRvAdapter : RecyclerView.Adapter<TenDaysRvAdapter.TenDaysVH>() {
    private var list: ArrayList<ForecastDay> = ArrayList()
    private var mTimeZone: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenDaysVH {
        return TenDaysVH(
            TenDayItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: TenDaysVH, position: Int) {
        val item = list[position]
        holder.clTenDays.setOnClickListener {
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

    class TenDaysVH(binding: TenDayItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvTenDate: TextView = binding.tvTenDate
        private val tvTenText: TextView = binding.tvTenText
        private val tvTenMaxTemp: TextView = binding.tvTenMaxTemp
        private val tvTenMinTemp: TextView = binding.tvTenMinTemp
        private val tvWindTen: TextView = binding.tvWindTen
        private val tvHumidityTen: TextView = binding.tvHumidityTen
        private val tvPressureTen: TextView = binding.tvPressureTen
        private val tvPopTen: TextView = binding.tvPopTen
        private val tvSunTen: TextView = binding.tvSunTen
        private val tvMoonTen: TextView = binding.tvMoonTen
        private val rvTenHourly: RecyclerView = binding.rvTenHourly
        private val tenIcon: ImageView = binding.tenIcon
        val ivArrow: ImageView = binding.ivArrow
        val expandableContainer: ConstraintLayout =
            binding.expandableContainer
        val clTenDays: ConstraintLayout = binding.clTenDays

        //TODO refactor  to mapper
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(forecastDay: ForecastDay, timeZone: String) {
            val sdf = SimpleDateFormat("dd.MM.yy")
            val day = forecastDay.day
            Glide
                .with(context)
                .load("https:${forecastDay.day?.condition?.icon}")
                .into(tenIcon)
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            tvTenDate.text = sdf.format(forecastDay.dateEpoch * 1000)
            tvTenText.text = day?.condition?.text.toString()
            tvTenMaxTemp.text = day?.maxTemp?.roundToInt().toString() + context.resources.getString(
                R.string.celsius
            )
            tvTenMinTemp.text = day?.minTemp?.roundToInt().toString() + context.resources.getString(
                R.string.celsius
            )
            tvWindTen.text = "${day?.maxWind} km/h"
            tvPressureTen.text = "${forecastDay.hour!![0].pressureMb} mb"
            tvHumidityTen.text = "${day?.avgHumidity.toString()}%"
            tvPopTen.text = "${day?.popRain.toString()}%, ${day?.totalPrecip.toString()} mm"
            tvSunTen.text = "${forecastDay.astro?.sunrise}\n${forecastDay.astro?.sunset}"
            tvMoonTen.text = "${forecastDay.astro?.moonrise}\n${forecastDay.astro?.moonset}"
            rvTenHourly.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val rvAdapter = HourAdapter()
            rvAdapter.updateList(forecastDay.hour!!, timeZone)
            rvTenHourly.adapter = rvAdapter
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