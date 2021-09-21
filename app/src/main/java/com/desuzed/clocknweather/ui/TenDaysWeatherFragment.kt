package com.desuzed.clocknweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.clocknweather.databinding.FragmentTenDaysWeatherBinding
import com.desuzed.clocknweather.mvvm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.WeatherViewModel
import com.desuzed.clocknweather.util.adapters.TenDaysRvAdapter


class TenDaysWeatherFragment : Fragment() {
    private var fragmentTenDaysWeatherBinding: FragmentTenDaysWeatherBinding? = null
    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(requireActivity().application)
        )
            .get(WeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTenDaysWeatherBinding =
            FragmentTenDaysWeatherBinding.inflate(inflater, container, false)
        return fragmentTenDaysWeatherBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvTenDays: RecyclerView = fragmentTenDaysWeatherBinding!!.rvTenDays
        rvTenDays.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val rvAdapter = TenDaysRvAdapter(requireContext())
        rvTenDays.adapter = rvAdapter
        weatherViewModel.weatherApiLiveData.observe(viewLifecycleOwner, {
            rvAdapter.updateList(it.forecast?.forecastday!!, it.location?.tzId.toString())
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        fragmentTenDaysWeatherBinding = null
    }
}