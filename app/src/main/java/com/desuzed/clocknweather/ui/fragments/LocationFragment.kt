package com.desuzed.clocknweather.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class LocationFragment : Fragment(), FavoriteLocationAdapter.OnItemClickListener {
    private lateinit var etCity: EditText
    private lateinit var rvCity: RecyclerView
    private lateinit var fabCurrentLocation: FloatingActionButton
    private lateinit var fabMapLocation: FloatingActionButton
    private lateinit var fragmentLocationBinding: FragmentLocationBinding
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
        bind()
        rvCity.adapter = favoriteLocationAdapter
        observeLiveData()
        etCity.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            val text = etCity.text.toString()
            if (text.isEmpty()) {
                //TODO клавиатура не сворачивается, неккоректно
                locationViewModel.onError(resources.getString(R.string.field_must_not_be_empty))
                return@OnEditorActionListener  false
            }
            if (actionId != EditorInfo.IME_ACTION_SEARCH ) {
                return@OnEditorActionListener false
            } else {
                locationViewModel.stateLiveData.postValue(StateRequest.Loading(text))
                navigateToMainFragment()
                hideKeyboard()
                return@OnEditorActionListener true
            }

        })
    }

    private fun observeLiveData (){
        locationViewModel.allLocations.observe(viewLifecycleOwner, {
            favoriteLocationAdapter.submitList(it)
        })
    }


    override fun onClick(favoriteLocationDto: FavoriteLocationDto) {
        val locationApp = LocationApp(
            favoriteLocationDto.lat.toFloat(),
            favoriteLocationDto.lon.toFloat(),
            favoriteLocationDto.cityName,
            favoriteLocationDto.region,
            favoriteLocationDto.country
        )
        locationViewModel.location.postValue(locationApp)
        navigateToMainFragment()
    }
//todo alert dialog
    override fun onLongClick(favoriteLocationDto: FavoriteLocationDto) {
        locationViewModel.deleteItem(favoriteLocationDto)
    }

    private fun bind() {
        etCity = fragmentLocationBinding.etCity
        rvCity = fragmentLocationBinding.rvCities
        fabMapLocation = fragmentLocationBinding.fabMapLocation
        fabMapLocation.setOnClickListener {
            showMapBotSheet()
        }
        fabCurrentLocation = fragmentLocationBinding.fabCurrentLocation
        fabCurrentLocation.setOnClickListener {
            (activity as MainActivity).locationHandler.postCurrentLocation()
            navigateToMainFragment()
            //networkViewModel.stateLiveData.postValue(StateRequest.Loading())
        }
    }

    private fun showMapBotSheet() {
        findNavController().navigate(R.id.action_locationFragment_to_mapBottomSheetFragment)
    }

    private fun navigateToMainFragment() {
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

}