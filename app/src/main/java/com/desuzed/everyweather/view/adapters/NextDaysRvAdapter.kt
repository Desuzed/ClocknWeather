package com.desuzed.everyweather.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.desuzed.everyweather.databinding.NextDayItemBinding
import com.desuzed.everyweather.model.model.ForecastDay
import com.desuzed.everyweather.util.editor.NextDaysEditor

class NextDaysRvAdapter : RecyclerView.Adapter<NextDaysRvAdapter.NextDaysVH>() {
    private var list: List<ForecastDay> = mutableListOf()
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
    fun updateList(newList: List<ForecastDay>, timeZone: String) {
        list = newList
        mTimeZone = timeZone
        notifyDataSetChanged()
    }

    class NextDaysVH(binding: NextDayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvNextDate: TextView = binding.tvNextDate
        private val tvNextDescription: TextView = binding.tvNextDescription
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
        private val expandableContainer: LinearLayout =
            binding.expandableContainer
        val clNextDays: CardView = binding.clNextDays

        fun bind(forecastDay: ForecastDay, timeZone: String) {
            val resultMap = NextDaysEditor(forecastDay, timeZone, itemView.context).getResultMap()
            Glide
                .with(itemView.context)
                .load(resultMap["nextIcon"])
                .into(nextIcon)
            tvNextDate.text = resultMap["tvNextDate"]
            tvNextDescription.text = resultMap["tvNextDescription"]
            tvNextMaxTemp.text = resultMap["tvNextMaxTemp"]
            tvNextMinTemp.text = resultMap["tvNextMinTemp"]
            tvNextWind.text = resultMap["tvNextWind"]
            tvNextPressure.text = resultMap["tvNextPressure"]
            tvNextHumidity.text = resultMap["tvNextHumidity"]
            tvNextPop.text = resultMap["tvNextPop"]
            tvNextSun.text = resultMap["tvNextSun"]
            tvNextMoon.text = resultMap["tvNextMoon"]
            rvNextHourly.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val rvAdapter = HourAdapter()
            rvAdapter.updateList(forecastDay.hourForecast, timeZone)
            rvNextHourly.adapter = rvAdapter
            expandCollapse(forecastDay.isExpanded)
        }

        private fun expandCollapse(isExpanded: Boolean) {
            when (isExpanded) {
                true -> {
                    expandableContainer.visibility = View.VISIBLE
                    ivArrow.rotation = 0f
                }
                false -> {
                    expandableContainer.visibility = View.GONE
                    ivArrow.rotation = 180f
                }
            }
        }
    }
}