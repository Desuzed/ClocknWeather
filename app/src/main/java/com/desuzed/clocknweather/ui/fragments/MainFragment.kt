package com.desuzed.clocknweather.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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


class MainFragment : Fragment() {
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
//        weatherViewModel.testLiveData.observe(requireActivity(), {
//            Log.i("TAG", "bind: $it")
//        })
        //TODO nonnull
        swipeRefresh.setOnRefreshListener {
            networkViewModel.queryLiveData.value?.let { networkViewModel.postForecast(it) }
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
        networkViewModel.queryLiveData.observe(requireActivity(), queryObserver)
        locationViewModel.location.observe(requireActivity(), locationObserver)
        networkViewModel.getNetworkLiveData().observe(requireActivity(), networkObserver)
        networkViewModel.refreshLiveData.observe(requireActivity(), refreshObserver)
//        weatherViewModel.errorMessage.observe(viewLifecycleOwner, {
//            if (weatherViewModel.errorMessage.isObserved){
//                return@observe
//            }
//            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
//            weatherViewModel.errorMessage.isObserved = true
//        })

    }




    private fun getQueryForecast(query: String) {
        //todo нужны ли здесь разрешения
        networkViewModel.postForecast(query)
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

    private val queryObserver = Observer<String?> {
        if (it != null) {
            getQueryForecast(it)
        } else {
            //TODO handle error
            Toast.makeText(
                requireContext(),
                "$it",
                Toast.LENGTH_LONG
            )
                .show()
        }
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
            if  (it.hasLocationInfo()){
                val query = "${it.cityName}, ${it.region}, ${it.country}"
                networkViewModel.queryLiveData.postValue(query)
                return@Observer
            }
            networkViewModel.queryLiveData.postValue(it.toString())
        } else {
            //TODO handle error
            Toast.makeText(
                requireContext(),
                "$it",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private val refreshObserver = Observer<Boolean> {
        swipeRefresh.isRefreshing = it
    }

    override fun onDestroy() {
        super.onDestroy()
        locationViewModel.location.removeObserver(locationObserver)
        networkViewModel.queryLiveData.removeObserver(queryObserver)
        networkViewModel.getNetworkLiveData().removeObserver(networkObserver)
        networkViewModel.refreshLiveData.removeObserver(refreshObserver)

    }


}