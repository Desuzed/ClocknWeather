package com.desuzed.everyweather.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.everyweather.R
import com.desuzed.everyweather.databinding.HourItemBinding
import com.desuzed.everyweather.mvvm.model.Hour
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class HourAdapter : RecyclerView.Adapter<HourAdapter.HourVH>() {
    private var list: ArrayList<Hour> = ArrayList()
    private var mTimeZone : String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourVH {
        return HourVH(
            HourItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HourVH, position: Int) {
        holder.bind(list[position], mTimeZone!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList (newList : ArrayList <Hour>, timeZone: String){
        list = newList
        mTimeZone = timeZone
        notifyDataSetChanged()
    }

    class HourVH (binding: HourItemBinding, val context : Context) :RecyclerView.ViewHolder(binding.root) {
        private val hTime: TextView = binding.hTime
        private val hTempC: TextView = binding.hTempC
        private val hWind: TextView = binding.hWind
        private val hIcon: ImageView = binding.hIcon
        private val hWindDegree: ImageView = binding.hWindDegree
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
                .load("https:${hour.icon}")
                .into(hIcon)
        }

    }
}
