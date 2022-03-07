package com.desuzed.everyweather.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.everyweather.databinding.NextDayItemBinding
import com.desuzed.everyweather.model.entity.ForecastDay
import com.desuzed.everyweather.util.editor.NextDaysEditor

class NextDaysRvAdapter :
    ListAdapter<ForecastDay, NextDaysRvAdapter.NextDaysVH>(NextDaysComparator()) {
    private var mTimeZone: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDaysVH {
        return NextDaysVH(
            NextDayItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NextDaysVH, position: Int) {
        val item = getItem(position)
        holder.clNextDays.setOnClickListener {
            val isExpanded = item.isExpanded
            item.isExpanded = !isExpanded
            notifyItemChanged(position)
        }
        holder.bind(item)
    }

    fun submitList(list: List<ForecastDay>?, timeZone: String) {
        super.submitList(list)
        mTimeZone = timeZone
    }

    inner class NextDaysVH(private val binding: NextDayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val clNextDays: CardView = binding.clNextDays

        fun bind(forecastDay: ForecastDay) {
            val resultMap =
                NextDaysEditor(forecastDay, mTimeZone!!, itemView.context).getResultMap()
            Glide
                .with(itemView.context)
                .load(resultMap["nextIcon"])
                .into(binding.nextIcon)
            binding.tvNextDate.text = resultMap["tvNextDate"]
            binding.tvNextDescription.text = resultMap["tvNextDescription"]
            binding.tvNextMaxTemp.text = resultMap["tvNextMaxTemp"]
            binding.tvNextMinTemp.text = resultMap["tvNextMinTemp"]
            binding.tvNextWind.text = resultMap["tvNextWind"]
            binding.tvNextPressure.text = resultMap["tvNextPressure"]
            binding.tvNextHumidity.text = resultMap["tvNextHumidity"]
            binding.tvNextPop.text = resultMap["tvNextPop"]
            binding.tvNextSun.text = resultMap["tvNextSun"]
            binding.tvNextMoon.text = resultMap["tvNextMoon"]
            binding.rvNextHourly.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val rvAdapter = HourAdapter()
            rvAdapter.submitList(forecastDay.hourForecast, mTimeZone!!)
            binding.rvNextHourly.adapter = rvAdapter
            expandCollapse(forecastDay.isExpanded)
        }

        private fun expandCollapse(isExpanded: Boolean) {
            when (isExpanded) {
                true -> {
                    binding.expandableContainer.visibility = View.VISIBLE
                    binding.ivArrow.rotation = 0f
                }
                false -> {
                    binding.expandableContainer.visibility = View.GONE
                    binding.ivArrow.rotation = 180f
                }
            }
        }
    }

    class NextDaysComparator : DiffUtil.ItemCallback<ForecastDay>() {
        override fun areItemsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean =
            oldItem.hashCode() == newItem.hashCode()

    }
}