package com.desuzed.everyweather.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.databinding.FavoriteLocationItemBinding

class FavoriteLocationAdapter(
    private val onItemClickListener: OnItemClickListener,
) :
    ListAdapter<FavoriteLocationDto, FavoriteLocationAdapter.FavoriteLocationVH>(LocationsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteLocationVH {
        return FavoriteLocationVH.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteLocationVH, position: Int) {
        val current = getItem(position)
        holder.bind(current, onItemClickListener)
    }

    class FavoriteLocationVH(private val binding: FavoriteLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            favoriteLocationDto: FavoriteLocationDto,
            onItemClickListener: OnItemClickListener
        ) {
            binding.tvFavoriteLocationName.text = favoriteLocationDto.cityName
            binding.tvFavoriteLocationRegionCountry.text = favoriteLocationDto.toString()
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
    interface OnItemClickListener {
        fun onClick(favoriteLocationDto: FavoriteLocationDto)
        fun onLongClick(favoriteLocationDto: FavoriteLocationDto)
    }

}