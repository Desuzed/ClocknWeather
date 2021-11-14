package com.desuzed.everyweather.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.desuzed.everyweather.App
import com.desuzed.everyweather.databinding.FragmentMainBinding
import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.vm.AppViewModelFactory
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
import com.desuzed.everyweather.ui.StateRequest


class MainFragment : Fragment() {
    //todo by lazy
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvInternetConnection: TextView
//    private val networkViewModel: NetworkViewModel by lazy {
//        ViewModelProvider(
//            requireActivity(),
//            AppViewModelFactory(App.instance)
//        )
//            .get(NetworkViewModel::class.java)
//    }
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
            sharedViewModel.queryLiveData.value?.let { getQueryForecast(it) }
        }

    }

    private fun bind() {
        tvInternetConnection = fragmentMainBinding.tvInternetConnection
        swipeRefresh = fragmentMainBinding.swipeRefresh
//        val bottomNavigationView = fragmentMainBinding.bottomNavigationView
//        val navController =
//            (childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
//        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    private fun observeLiveData() {
        sharedViewModel.getCachedForecast()
        sharedViewModel.loadCachedQuery()
        sharedViewModel.stateLiveData.observe(viewLifecycleOwner, stateObserver)
        sharedViewModel.queryLiveData.observe(viewLifecycleOwner, queryObserver)
        sharedViewModel.location.observe(viewLifecycleOwner, locationObserver)
        sharedViewModel.getNetworkLiveData().observe(viewLifecycleOwner, networkObserver)


    }

    private fun getQueryForecast(query: String) {
        sharedViewModel.getForecast(query)
    }


    fun launchRefresh(state: Boolean) {
        swipeRefresh.isRefreshing = state
    }

    private val stateObserver = Observer<StateRequest> {
        when (it) {
            is StateRequest.Loading -> {
                launchRefresh(true)
            }
            is StateRequest.Success -> {
                if (it.toggleSaveButton) {
                        toggleSaveButton(true)
                    } else {
                        toggleSaveButton(false)
                    }
                launchRefresh(false)
            }
            is StateRequest.Error -> {
                launchRefresh(false)
                toggleSaveButton(false)
                Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
//            is StateRequest.NoData -> {
//                launchRefresh(false)
//                toggleSaveButton(false)
//            }
        }
    }

    private val queryObserver = Observer<String> {
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
            postQuery(it.toString())
        }
    }

    private fun toggleSaveButton(state: Boolean) {
        sharedViewModel.toggleSaveButton(state)

    }

    private fun postQuery(query: String) {
        sharedViewModel.postQuery(query)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.location.removeObserver(locationObserver)
        sharedViewModel.queryLiveData.removeObserver(queryObserver)
        sharedViewModel.getNetworkLiveData().removeObserver(networkObserver)
//        networkViewModel.stateLiveData.removeObserver(networkStateObserver)
        sharedViewModel.stateLiveData.removeObserver(stateObserver)
//        locationViewModel.stateLiveData.removeObserver(locationStateObserver)

    }


}