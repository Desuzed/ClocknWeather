package com.desuzed.everyweather.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.model.LocationApp
import com.desuzed.everyweather.model.model.Location
import com.desuzed.everyweather.model.vm.AppViewModelFactory
import com.desuzed.everyweather.model.vm.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
class MapBottomSheetFragment : BottomSheetDialogFragment(), OnMapReadyCallback {
    private var job: Job? = null
    private var location : Location? = null

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
    ): View? {
        return inflater.inflate(R.layout.frag_map_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        //todo mock
        location = requireArguments().getParcelable<Location>("key")
            val oldMarker = instantiateOldMarker(location, googleMap)
            googleMap.setOnMapClickListener { latLng ->
                showAlertDialog(latLng, googleMap, oldMarker)
        }
    }


    private fun instantiateOldMarker (location: Location?, googleMap: GoogleMap): Marker? {
        return if (location == null){
            null
        }else {
            val latlng = LatLng(location.lat.toDouble(), location.lon.toDouble())
            val markerOptions = MarkerOptions()
                .position(latlng)
                .title(location.name)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11f))
            googleMap.addMarker(markerOptions)
        }
    }


    private fun showAlertDialog(latLng: LatLng, googleMap: GoogleMap, oldMarker: Marker?) {
        val dialog = MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_MaterialAlertDialog)
            .setTitle(resources.getString(R.string.load_weather_of_this_location))
            .setPositiveButton(resources.getString(R.string.ok)) { alertDialog, _ ->
                job = CoroutineScope(Dispatchers.Main).launch {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                    )
                    oldMarker?.remove()
                    alertDialog.dismiss()
                    delay(1000)
                    val locationApp =
                        LocationApp(latLng.latitude.toFloat(), latLng.longitude.toFloat())
                    sharedViewModel.postLocation(locationApp)
                    dismiss()
                    navigateToMainFragment()
                }
            }
            .setNeutralButton(resources.getString(R.string.cancel)) { alertDialog, _ ->
                alertDialog.dismiss()
            }.create()
        dialog.show()
    }


    private fun navigateToMainFragment() {
        navigate(R.id.action_mapBottomSheetFragment_to_weatherFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}