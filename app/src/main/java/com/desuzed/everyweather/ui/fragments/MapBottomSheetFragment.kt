package com.desuzed.everyweather.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.vm.AppViewModelFactory
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
//TODO Передавать первичные координаты с навигацией
class MapBottomSheetFragment : BottomSheetDialogFragment(), OnMapReadyCallback {
    private var job: Job? = null
//    private val locationViewModel: LocationViewModel by lazy {
//        ViewModelProvider(
//            requireActivity(),
//            AppViewModelFactory(App.instance)
//        )
//            .get(LocationViewModel::class.java)
//    }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_map_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    //TODO refactor. Refactor alert dialog , make custom
    override fun onMapReady(googleMap: GoogleMap) {
       // val location = locationViewModel.location.value
        val location = sharedViewModel.weatherApiLiveData.value?.location
        if (location == null) {
            return
        } else {
            val latlng = LatLng(location.lat.toDouble(), location.lon.toDouble())
            val markerOptions = MarkerOptions()
                .position(latlng)
                .title(location.name)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11f))
            val oldMarker = googleMap.addMarker(markerOptions)
            googleMap.setOnMapClickListener { latLng ->
                showAlertDialog(latLng, googleMap, oldMarker)
            }
        }
    }


    private fun showAlertDialog(latLng: LatLng, googleMap: GoogleMap, oldMarker: Marker) {
        val alertDialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.set_new_location)
                setPositiveButton(R.string.ok) { _, _ ->
                    job = CoroutineScope(Dispatchers.Main).launch {
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                        )
                        oldMarker.remove()
                        delay(1000)
                        //TODO delegate to mapper
                        val locationApp = LocationApp(latLng.latitude.toFloat(), latLng.longitude.toFloat())
                        sharedViewModel.postLocation(locationApp)
                        dismiss()
                        navigateToMainFragment()
                    }
                }
                setNegativeButton(R.string.cancel) { _, _ ->

                }
            }
            builder.create()
        }
        alertDialog.show()
    }

//    private fun postLocationVisibility(state : Boolean){
//        locationViewModel.toggleSaveButton(state)
//    }

    private fun navigateToMainFragment (){
        findNavController().navigate(R.id.action_mapBottomSheetFragment_to_weatherFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()

    }
}