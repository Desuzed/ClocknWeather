package com.desuzed.everyweather.view.fragments.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.data.room.FavoriteLocationMapper
import com.desuzed.everyweather.databinding.FragmentLocationBinding
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.main_activity.MainActivity
import com.desuzed.everyweather.view.adapters.FavoriteLocationAdapter
import com.desuzed.everyweather.view.fragments.addOnBackPressedCallback
import com.desuzed.everyweather.view.fragments.navigate
import com.desuzed.everyweather.view.fragments.toast
import com.desuzed.everyweather.view.fragments.weather.main.WeatherMainFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class LocationFragment : Fragment(), FavoriteLocationAdapter.OnItemClickListener {
    private lateinit var binding: FragmentLocationBinding
    private val favoriteLocationAdapter by lazy { FavoriteLocationAdapter(this) }
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(LocationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnBackPressedCallback()
        onClickListeners()
        observeLiveData()
        setOnEditTextListener()
        binding.rvCities.adapter = favoriteLocationAdapter
    }

    private fun setOnEditTextListener() {
        binding.etCity.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            val text = binding.etCity.text.toString()
            if (text.isEmpty()) {
                locationViewModel.onError(ActionResultProvider.EMPTY_FIELD)
                hideKeyboard()
                return@OnEditorActionListener false
            }
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                locationViewModel.onError(ActionResultProvider.FAIL)
                return@OnEditorActionListener false
            } else {
                val bundle = bundleOf(WeatherMainFragment.QUERY_KEY to text)
                navigateToWeatherFragment(bundle)
                hideKeyboard()
                return@OnEditorActionListener true
            }

        })
    }

    private fun observeLiveData() {
        locationViewModel.allLocations.observe(viewLifecycleOwner, allLocationObserver)
        locationViewModel.messageLiveData.observe(viewLifecycleOwner, { event ->
            if (event.hasBeenHandled) return@observe
            event.getContentIfNotHandled()?.let { message -> toast(message) }
        })
    }

    private val allLocationObserver = Observer<List<FavoriteLocationDto>> {
        favoriteLocationAdapter.submitList(it)
        binding.rvCities.startLayoutAnimation()
        if (it.isEmpty()) {
            toggleEmptyList(true)
        } else {
            toggleEmptyList(false)
        }
    }


    override fun onClick(favoriteLocationDto: FavoriteLocationDto) {
        val userLatLng = FavoriteLocationMapper().mapFromEntity(favoriteLocationDto)
        val bundle = bundleOf(WeatherMainFragment.QUERY_KEY to userLatLng.toString())
        navigateToWeatherFragment(bundle)
    }

    override fun onLongClick(favoriteLocationDto: FavoriteLocationDto) {
        showAlertDialog(favoriteLocationDto)
    }

    private fun showAlertDialog(favoriteLocationDto: FavoriteLocationDto) {
        val dialog =
            MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_MaterialAlertDialog)
                .setTitle(resources.getString(R.string.delete))
                .setPositiveButton(resources.getString(R.string.ok)) { alertDialog, _ ->
                    locationViewModel.deleteItem(favoriteLocationDto)
                    alertDialog.dismiss()
                }
                .setNeutralButton(resources.getString(R.string.cancel)) { alertDialog, _ ->
                    alertDialog.dismiss()
                }.create()
        dialog.show()
    }

    private fun onClickListeners() {
        binding.btnMapLocation.setOnClickListener {
            showMapBotSheet()
        }
        binding.fabCurrentLocation.setOnClickListener {
            (activity as MainActivity).locationHandler.findUserLocation()
            val bundle = bundleOf(WeatherMainFragment.USER_LOCATION to true)
            navigateToWeatherFragment(bundle)
        }
    }

    private fun showMapBotSheet() {
        navigate(R.id.action_locationFragment_to_mapBottomSheetFragment)
    }

    private fun navigateToWeatherFragment(bundle: Bundle) {
        navigate(R.id.action_locationFragment_to_weatherFragment, bundle)
    }

    private fun hideKeyboard() {
        val activity = requireActivity()
        if (activity.currentFocus == null) {
            return
        }
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    private fun toggleEmptyList(isListEmpty: Boolean) {
        when (isListEmpty) {
            true -> {
                binding.rvCities.visibility = View.GONE
                binding.tvEmptyList.visibility = View.VISIBLE
            }
            false -> {
                binding.rvCities.visibility = View.VISIBLE
                binding.tvEmptyList.visibility = View.GONE
            }
        }
    }
}