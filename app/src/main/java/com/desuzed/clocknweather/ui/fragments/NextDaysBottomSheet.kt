package com.desuzed.clocknweather.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.adapters.TenDaysRvAdapter
import com.desuzed.clocknweather.databinding.FragmentNextDaysBotSheetBinding
import com.desuzed.clocknweather.databinding.FragmentWeatherMainBinding
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.vm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.vm.LocationViewModel
import com.desuzed.clocknweather.mvvm.vm.NetworkViewModel
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class NextDaysBottomSheet : BottomSheetDialogFragment() {
    private val networkViewModel: NetworkViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(NetworkViewModel::class.java)
    }
    private val tenAdapter by lazy { TenDaysRvAdapter() }
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
        networkViewModel.weatherApiLiveData.observe(viewLifecycleOwner, weatherObserver)
    }

    private val weatherObserver = Observer<WeatherApi?> { response ->
        if (response == null) {
            return@Observer
        }
        updateUi(response)
    }

    private fun updateUi(response: WeatherApi) {
        tenAdapter.updateList(
            response.forecast?.forecastday!!,
            response.locationDto?.tzId.toString()
        )
    }


}