package com.desuzed.everyweather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.everyweather.databinding.HourItemBinding
import com.desuzed.everyweather.model.entity.Hour
import com.desuzed.everyweather.view.entity.HourEntityView

class HourAdapter : ListAdapter<Hour, HourAdapter.HourVH>(HourComparator()) {
    private var mTimeZone: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourVH {
        return HourVH(
            HourItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HourVH, position: Int) {
        holder.bind(getItem(position))
    }

    fun submitList(list: List<Hour>, timeZone: String) {
        super.submitList(list)
        mTimeZone = timeZone
    }


    inner class HourVH(private val binding: HourItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hour: Hour) {
            val hourEntityView = HourEntityView(hour, mTimeZone!!, itemView.resources )
            binding.hTime.text = hourEntityView.time
            binding.hTempC.text = hourEntityView.temp
            binding.hWind.text = hourEntityView.wind
            binding.hWindDegree.rotation = hourEntityView.rotation
            Glide
                .with(itemView.context)
                .load(hourEntityView.iconUrl)
                .into(binding.hIcon)
        }

    }

    class HourComparator : DiffUtil.ItemCallback<Hour>() {
        override fun areContentsTheSame(oldItem: Hour, newItem: Hour): Boolean =
            oldItem.hashCode() == newItem.hashCode()


        override fun areItemsTheSame(oldItem: Hour, newItem: Hour): Boolean =
            oldItem == newItem


    }
}
