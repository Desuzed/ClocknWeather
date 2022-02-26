package com.desuzed.everyweather.view.fragments.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.desuzed.everyweather.App
import com.desuzed.everyweather.databinding.FragmentNextDaysBotSheetBinding
import com.desuzed.everyweather.model.model.WeatherResponse
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.SharedViewModel
import com.desuzed.everyweather.view.adapters.NextDaysRvAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class NextDaysBottomSheet : BottomSheetDialogFragment() {
    private val tenAdapter by lazy { NextDaysRvAdapter() }
    private lateinit var binding: FragmentNextDaysBotSheetBinding
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(SharedViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNextDaysBotSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvTen = binding.rvTenDaysMain
        rvTen.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvTen.adapter = tenAdapter
        sharedViewModel.weatherApiLiveData.observe(viewLifecycleOwner, weatherObserver)
    }

    private val weatherObserver = Observer<WeatherResponse?> { response ->
        if (response == null) {
            return@Observer
        }
        updateUi(response)
    }

    private fun updateUi(response: WeatherResponse) {
        tenAdapter.submitList(
            response.forecastDay,
            response.location.tzId
        )
    }
}