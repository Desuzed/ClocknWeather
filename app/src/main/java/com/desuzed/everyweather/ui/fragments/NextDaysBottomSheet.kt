package com.desuzed.everyweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.desuzed.everyweather.App
import com.desuzed.everyweather.adapters.NextDaysRvAdapter
import com.desuzed.everyweather.databinding.FragmentNextDaysBotSheetBinding
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.vm.AppViewModelFactory
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class NextDaysBottomSheet : BottomSheetDialogFragment() {
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(SharedViewModel::class.java)
    }

//    private val networkViewModel: NetworkViewModel by lazy {
//        ViewModelProvider(
//            requireActivity(),
//            AppViewModelFactory(App.instance)
//        )
//            .get(NetworkViewModel::class.java)
//    }
    private val tenAdapter by lazy { NextDaysRvAdapter() }
    private lateinit var binding: FragmentNextDaysBotSheetBinding

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
        tenAdapter.updateList(
            response.forecastDay,
            response.location.tzId
        )
    }


}