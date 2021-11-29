package com.desuzed.everyweather.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.adapters.FavoriteLocationAdapter
import com.desuzed.everyweather.databinding.FragmentLocationBinding
import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.everyweather.mvvm.vm.AppViewModelFactory
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
import com.desuzed.everyweather.ui.MainActivity
import com.desuzed.everyweather.util.addOnBackPressedCallback
import com.desuzed.everyweather.util.navigate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class LocationFragment : Fragment(), FavoriteLocationAdapter.OnItemClickListener {
    val TAG = "Location"
    private lateinit var fragmentLocationBinding: FragmentLocationBinding
    private lateinit var etCity: EditText
    private lateinit var rvCity: RecyclerView
    private lateinit var tvEmptyList: TextView
    private lateinit var fabCurrentLocation: FloatingActionButton
    private lateinit var btnMapLocation: Button
    private val favoriteLocationAdapter by lazy { FavoriteLocationAdapter(this) }
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(SharedViewModel::class.java)
    }

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
        addOnBackPressedCallback()
        onClickListeners()
        observeLiveData()
        setOnEditTextListener()
    }

    private fun bind() {
        etCity = fragmentLocationBinding.etCity
        tvEmptyList = fragmentLocationBinding.tvEmptyList
        fabCurrentLocation = fragmentLocationBinding.fabCurrentLocation
        btnMapLocation = fragmentLocationBinding.btnMapLocation
        rvCity = fragmentLocationBinding.rvCities
        rvCity.adapter = favoriteLocationAdapter
    }

    private fun setOnEditTextListener() {
        etCity.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            val text = etCity.text.toString()
            if (text.isEmpty()) {
                sharedViewModel.onError(resources.getString(R.string.field_must_not_be_empty))
                hideKeyboard()
                return@OnEditorActionListener false
            }
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                sharedViewModel.onError(resources.getString(R.string.internal_app_error))
                return@OnEditorActionListener false
            } else {
                sharedViewModel.postQuery(text)
                navigateToWeatherFragment()
                hideKeyboard()
                return@OnEditorActionListener true
            }

        })
    }

    private fun observeLiveData() {
        sharedViewModel.allLocations.observe(viewLifecycleOwner, allLocationObserver)

    }

    private val allLocationObserver = Observer<List<FavoriteLocationDto>> {
        favoriteLocationAdapter.submitList(it)
        rvCity.startLayoutAnimation()
        //TODO неккоректно отрабатывает анимация, когнда переходишь на фрагмент, поэтом приходится вызывать ее вручную
        if (it.isEmpty()) {
            toggleEmptyList(true)
        } else {
            toggleEmptyList(false)
        }
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
        sharedViewModel.postLocation(locationApp)
        navigateToWeatherFragment()
    }

    //todo alert dialog
    override fun onLongClick(favoriteLocationDto: FavoriteLocationDto) {
        sharedViewModel.deleteItem(favoriteLocationDto)
    }

    private fun onClickListeners() {
        btnMapLocation.setOnClickListener {
            showMapBotSheet()
        }
        fabCurrentLocation.setOnClickListener {
            (activity as MainActivity).locationHandler.postCurrentLocation()
            navigateToWeatherFragment()
        }
    }

    private fun showMapBotSheet() {
        navigate(R.id.action_locationFragment_to_mapBottomSheetFragment)
    }

    private fun navigateToWeatherFragment() {
        // findNavController().navigate(R.id.action_locationFragment_to_weatherFragment)
        navigate(R.id.action_locationFragment_to_weatherFragment)

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
                rvCity.visibility = View.INVISIBLE
                tvEmptyList.visibility = View.VISIBLE
            }
            false -> {
                rvCity.visibility = View.VISIBLE
                tvEmptyList.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.allLocations.removeObserver(allLocationObserver)
    }
}