package com.desuzed.clocknweather.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.adapters.FavoriteLocationAdapter
import com.desuzed.clocknweather.databinding.FragmentLocationBinding
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.clocknweather.mvvm.vm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.vm.LocationViewModel
import com.desuzed.clocknweather.ui.MainActivity
import com.desuzed.clocknweather.ui.StateRequest
import java.util.*


class LocationFragment : Fragment(), FavoriteLocationAdapter.OnItemClickListener {
    private lateinit var fragmentLocationBinding: FragmentLocationBinding
    private val etCity by lazy { fragmentLocationBinding.etCity }
    private val rvCity by lazy { fragmentLocationBinding.rvCities }
    private val tvEmptyList by lazy { fragmentLocationBinding.tvEmptyList }
    private val fabCurrentLocation by lazy { fragmentLocationBinding.fabCurrentLocation }
    private val fabMapLocation by lazy { fragmentLocationBinding.fabMapLocation }
    private val favoriteLocationAdapter by lazy { FavoriteLocationAdapter(this) }
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(LocationViewModel::class.java)
    }

//
//    private val networkViewModel: NetworkViewModel by lazy {
//        ViewModelProvider(
//            requireActivity(),
//            AppViewModelFactory(App.instance)
//        )
//            .get(NetworkViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLocationBinding = FragmentLocationBinding.inflate(inflater, container, false)
        return fragmentLocationBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        rvCity.adapter = favoriteLocationAdapter
        observeLiveData()
        etCity.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            val text = etCity.text.toString()
            if (text.isEmpty()) {
                locationViewModel.onError(resources.getString(R.string.field_must_not_be_empty)) //TODO refactor to shared VM
                hideKeyboard()
                return@OnEditorActionListener  false
            }
            if (actionId != EditorInfo.IME_ACTION_SEARCH ) {
                hideKeyboard()
                locationViewModel.onError(resources.getString(R.string.internal_app_error)) //TODO refactor to shared VM
                return@OnEditorActionListener false
            } else {
                locationViewModel.stateLiveData.postValue(StateRequest.Loading(text))
                navigateToWeatherFragment()
                hideKeyboard()
                return@OnEditorActionListener true
            }

        })
    }

    private fun observeLiveData (){
        locationViewModel.allLocations.observe(viewLifecycleOwner, {
            favoriteLocationAdapter.submitList(it)
            if (it.isEmpty()){
                toggleEmptyList(true)
            }else {
                toggleEmptyList(false)
            }
        })
    }

//TODO refactor to mapper
    override fun onClick(favoriteLocationDto: FavoriteLocationDto) {
        val locationApp = LocationApp(
            favoriteLocationDto.lat.toFloat(),
            favoriteLocationDto.lon.toFloat(),
            favoriteLocationDto.cityName,
            favoriteLocationDto.region,
            favoriteLocationDto.country
        )
        locationViewModel.location.postValue(locationApp)
        navigateToWeatherFragment()
    }
//todo alert dialog
    override fun onLongClick(favoriteLocationDto: FavoriteLocationDto) {
        locationViewModel.deleteItem(favoriteLocationDto)
    }

    private fun onClickListeners() {
        fabMapLocation.setOnClickListener {
            showMapBotSheet()
        }
        fabCurrentLocation.setOnClickListener {
            (activity as MainActivity).locationHandler.postCurrentLocation()
            navigateToWeatherFragment()
            //networkViewModel.stateLiveData.postValue(StateRequest.Loading())
        }
    }

    private fun showMapBotSheet() {
        findNavController().navigate(R.id.action_locationFragment_to_mapBottomSheetFragment)
    }

    private fun navigateToWeatherFragment() {
        findNavController().navigate(R.id.action_locationFragment_to_weatherFragment)
    }
    fun hideKeyboard() {
        val activity = requireActivity()
        if (activity.currentFocus == null){
            return
        }
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    private fun toggleEmptyList (isListEmpty : Boolean){
        when (isListEmpty){
            true ->{
                rvCity.visibility = View.GONE
                tvEmptyList.visibility = View.VISIBLE
            }
            false ->{
                rvCity.visibility = View.VISIBLE
                tvEmptyList.visibility = View.GONE
            }
        }
    }

}