package com.desuzed.clocknweather.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.databinding.FavoriteLocationItemBinding
import com.desuzed.clocknweather.databinding.TenDayItemBinding
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto

class FavoriteLocationAdapter(
    val onItemClickListener: OnItemClickListener,
) :
    ListAdapter<FavoriteLocationDto, FavoriteLocationAdapter.FavoriteLocationVH>(LocationsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteLocationVH {
        return FavoriteLocationVH.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteLocationVH, position: Int) {
        val current = getItem(position)
        holder.bind(current, onItemClickListener)
    }

    class FavoriteLocationVH(binding: FavoriteLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvFavoriteLocationName: TextView = binding.tvFavoriteLocationName
        private val tvFavoriteLocationRegionCountry: TextView =
            binding.tvFavoriteLocationRegionCountry

        @SuppressLint("SetTextI18n")
        fun bind(
            favoriteLocationDto: FavoriteLocationDto,
            onItemClickListener: OnItemClickListener
        ) {
            tvFavoriteLocationName.text = favoriteLocationDto.cityName
            tvFavoriteLocationRegionCountry.text =
                "${favoriteLocationDto.region}, ${favoriteLocationDto.country}"
            itemView.setOnClickListener {
                onItemClickListener.onClick(favoriteLocationDto)
            }
            itemView.setOnLongClickListener {
                onItemClickListener.onLongClick(favoriteLocationDto)
                return@setOnLongClickListener true
            }
        }


        companion object {
            fun create(parent: ViewGroup): FavoriteLocationVH {
                return FavoriteLocationVH(
                    FavoriteLocationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    class LocationsComparator : DiffUtil.ItemCallback<FavoriteLocationDto>() {
        override fun areContentsTheSame(
            oldItem: FavoriteLocationDto,
            newItem: FavoriteLocationDto
        ): Boolean {
            return oldItem.latLon == newItem.latLon
        }

        override fun areItemsTheSame(
            oldItem: FavoriteLocationDto,
            newItem: FavoriteLocationDto
        ): Boolean {
            return oldItem == newItem
        }

    }
//todo Refactor to high orer func
    interface OnItemClickListener {
        fun onClick(favoriteLocationDto: FavoriteLocationDto)
        fun onLongClick(favoriteLocationDto: FavoriteLocationDto)
    }

}