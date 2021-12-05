package com.desuzed.everyweather.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.everyweather.databinding.HourItemBinding
import com.desuzed.everyweather.mvvm.model.Hour
import com.desuzed.everyweather.util.editor.HourStringEditor

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

        fun bind(hour: Hour, timeZone: String) {
          val resultMap = HourStringEditor(hour, timeZone, context).getResultMap()
            hTime.text = resultMap["hTime"]
            hTempC.text = resultMap["hTempC"]
            hWind.text = resultMap["hWind"]
            hWindDegree.rotation = hour.windDegree.toFloat() - 180
            Glide
                .with(context)
                .load(resultMap["hIcon"])
                .into(hIcon)
        }

    }
}
