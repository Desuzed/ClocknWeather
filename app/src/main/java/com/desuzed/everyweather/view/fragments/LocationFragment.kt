package com.desuzed.everyweather.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.everyweather.App
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.data.room.FavoriteLocationMapper
import com.desuzed.everyweather.databinding.FragmentLocationBinding
import com.desuzed.everyweather.model.model.Location
import com.desuzed.everyweather.model.vm.AppViewModelFactory
import com.desuzed.everyweather.model.vm.LocationViewModel
import com.desuzed.everyweather.model.vm.SharedViewModel
import com.desuzed.everyweather.view.MainActivity
import com.desuzed.everyweather.view.adapters.FavoriteLocationAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class LocationFragment : Fragment(), FavoriteLocationAdapter.OnItemClickListener {
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
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(SharedViewModel::class.java)
    }

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

    fun setStatusBarColor(color: Int) {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.statusBarColor = color
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
                locationViewModel.onError(ActionResultProvider.EMPTY_FIELD)
                hideKeyboard()
                return@OnEditorActionListener false
            }
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                locationViewModel.onError(ActionResultProvider.FAIL)
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
        locationViewModel.allLocations.observe(viewLifecycleOwner, allLocationObserver)
        locationViewModel.messageLiveData.observe(viewLifecycleOwner, messageObserver)
    }

    private val messageObserver = Observer<String> {
        toast(it)
    }

    private val allLocationObserver = Observer<List<FavoriteLocationDto>> {
        favoriteLocationAdapter.submitList(it)
        rvCity.startLayoutAnimation()
        if (it.isEmpty()) {
            toggleEmptyList(true)
        } else {
            toggleEmptyList(false)
        }
    }


    override fun onClick(favoriteLocationDto: FavoriteLocationDto) {
        val locationApp = FavoriteLocationMapper().mapFromEntity(favoriteLocationDto)
        sharedViewModel.postLocation(locationApp)
        navigateToWeatherFragment()
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
        btnMapLocation.setOnClickListener {
            showMapBotSheet()
        }
        fabCurrentLocation.setOnClickListener {
            (activity as MainActivity).locationHandler.postCurrentLocation()
            navigateToWeatherFragment()
        }
    }

    private fun showMapBotSheet() {
        //todo mock
        val bundle = bundleOf("key" to Location("1", "2", "3", 44.1f, 131.1f, "4", 10000, "st"))
        navigate(R.id.action_locationFragment_to_mapBottomSheetFragment, bundle)
    }

    private fun navigateToWeatherFragment() {
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
        locationViewModel.allLocations.removeObserver(allLocationObserver)
    }
}