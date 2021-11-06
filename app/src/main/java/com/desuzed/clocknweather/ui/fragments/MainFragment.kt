package com.desuzed.clocknweather.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.databinding.FragmentMainBinding
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.vm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.vm.LocationViewModel
import com.desuzed.clocknweather.mvvm.vm.NetworkViewModel
import com.desuzed.clocknweather.mvvm.vm.SharedViewModel
import com.desuzed.clocknweather.network.model.Query
import com.desuzed.clocknweather.ui.StateRequest


class MainFragment : Fragment() {
    //todo by lazy
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvInternetConnection: TextView
    private val networkViewModel: NetworkViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(NetworkViewModel::class.java)
    }
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance)
        )
            .get(LocationViewModel::class.java)
    }

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        observeLiveData()

        //TODO nonnull
        swipeRefresh.setOnRefreshListener {
            networkViewModel.queryLiveData.value?.let { getQueryForecast(it) }
        }

    }

    private fun bind() {
        tvInternetConnection = fragmentMainBinding.tvInternetConnection
        swipeRefresh = fragmentMainBinding.swipeRefresh
        val bottomNavigationView = fragmentMainBinding.bottomNavigationView
        val navController =
            (childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    private fun observeLiveData() {
        networkViewModel.getCachedForecast()
        networkViewModel.loadCachedQuery()
        networkViewModel.stateLiveData.observe(viewLifecycleOwner, networkStateObserver)
        locationViewModel.stateLiveData.observe(viewLifecycleOwner, locationStateObserver)
        sharedViewModel.stateLiveData.observe(viewLifecycleOwner, stateObserver)
        networkViewModel.queryLiveData.observe(viewLifecycleOwner, queryObserver)
        locationViewModel.location.observe(viewLifecycleOwner, locationObserver)
        networkViewModel.getNetworkLiveData().observe(viewLifecycleOwner, networkObserver)


    }

    private val networkStateObserver = Observer<StateRequest> {
        sharedViewModel.stateLiveData.postValue(it)
    }

    private val locationStateObserver = Observer<StateRequest> {
        sharedViewModel.stateLiveData.postValue(it)
    }

    private fun getQueryForecast(query: Query) {
        //todo нужны ли здесь разрешения
        networkViewModel.getForecast(query)
        //  Log.i("onceObserved", ": true")
        //   locationViewModel.getLocationLiveData().isObserved = true
//        Toast.makeText(
//            this,
//            "Требуется разрешение на местоположение",
//            Toast.LENGTH_LONG
//        )
//            .show()
//        requestLocationPermissions()
    }


    fun launchRefresh(state: Boolean) {
        swipeRefresh.isRefreshing = state
    }

    private val stateObserver = Observer<StateRequest> {
        when (it) {
            is StateRequest.Loading -> {
                Log.d("StateUi", ":Loading ")
                launchRefresh(true)
                if (it.hasQuery()) {
                    postQuery(Query(it.query, true))
                }
            }
            is StateRequest.Success -> {
                Log.d("StateUi", ": Success : ${it.successData}")
                if (it.hasData()) {
                    val saved = locationViewModel.isSaved(it.successData)
                    if (saved){
                        sharedViewModel.toggleSaveButton(false) //TODO refactor
                    }else {
                        sharedViewModel.toggleSaveButton(true) //TODO refactor
                    }
                }
                launchRefresh(false)
            }
            is StateRequest.Error -> {
                Log.d("StateUi", ": Error")
                launchRefresh(false)
                sharedViewModel.toggleSaveButton(false) //TODO refactor
                Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            is StateRequest.NoData -> Log.d(
                "StateUi",
                ": NoData"
            ) //TODO empty fragment with message no information
        }
    }

    private val queryObserver = Observer<Query> {
        getQueryForecast(it)
    }

    private val networkObserver = Observer<Boolean> {
        when (it) {
            true -> {
                tvInternetConnection.visibility = View.GONE
            }
            else -> {
                tvInternetConnection.visibility = View.VISIBLE
            }
        }
    }

    private val locationObserver = Observer<LocationApp> {
        if (it != null) {
//            if (it.hasLocationInfo()) {               //TODO выдает арсеньев вместо владивостока, если искать по полям
//                postQuery(Query(it.toStringInfoFields()))
//                return@Observer
//            }
            postQuery(Query(it.toString()))
        }
    }

    private fun postQuery(query: Query) {
        networkViewModel.postQuery(query)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationViewModel.location.removeObserver(locationObserver)
        networkViewModel.queryLiveData.removeObserver(queryObserver)
        networkViewModel.getNetworkLiveData().removeObserver(networkObserver)
        networkViewModel.stateLiveData.removeObserver(networkStateObserver)
        sharedViewModel.stateLiveData.removeObserver(stateObserver)
        locationViewModel.stateLiveData.removeObserver(locationStateObserver)

    }


}