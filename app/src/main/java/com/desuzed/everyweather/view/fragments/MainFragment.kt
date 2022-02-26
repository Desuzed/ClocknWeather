package com.desuzed.everyweather.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.databinding.FragmentMainBinding
import com.desuzed.everyweather.model.model.LocationApp
import com.desuzed.everyweather.view.AppViewModelFactory
import com.desuzed.everyweather.view.SharedViewModel
import com.desuzed.everyweather.view.StateUI
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        binding.swipeRefresh.setOnRefreshListener {
            sharedViewModel.queryLiveData.value?.let {
                getQueryForecast(it)
            }
        }
        tryLoadAd()
    }

    private fun tryLoadAd (){
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }


    private fun observeLiveData() {
        sharedViewModel.loadCachedQuery()
        sharedViewModel.stateLiveData.observe(viewLifecycleOwner, stateObserver)
        sharedViewModel.queryLiveData.observe(viewLifecycleOwner, queryObserver)
        sharedViewModel.location.observe(viewLifecycleOwner, locationObserver)
        sharedViewModel.getNetworkLiveData().observe(viewLifecycleOwner, networkObserver)
    }

    private fun getQueryForecast(query: String) {
        sharedViewModel.getForecast(query)
    }


    private fun launchRefresh(state: Boolean) {
        binding.swipeRefresh.isRefreshing = state
    }

    private fun toast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        )
            .show()
    }

    private val stateObserver = Observer<StateUI> {
        when (it) {
            is StateUI.Loading -> {
                launchRefresh(true)
            }
            is StateUI.Success -> {
                onSuccess(it)
            }
            is StateUI.Error -> {
                onError(it)
            }
            is StateUI.NoData -> {
                launchRefresh(false)
                toggleSaveButton(false)
            }
        }
    }

    private fun onSuccess(it: StateUI.Success) {
        if (it.toggleSaveButton) toggleSaveButton(true)
        else toggleSaveButton(false)
        if (it.message.isNotEmpty()) {
            toast(it.message)
        }
        launchRefresh(false)
    }

    private fun onError(it: StateUI.Error) {
        launchRefresh(false)
        toggleSaveButton(false)
        toast(it.message)
    }

    private val queryObserver = Observer<String> {
        getQueryForecast(it)
    }

    private val networkObserver = Observer<Boolean> {
        when (it) {
            true -> {
                binding.tvInternetConnection.visibility = View.GONE
              //  tryLoadAd()
            }
            else -> {
                binding.tvInternetConnection.visibility = View.VISIBLE
            }
        }
    }

    private val locationObserver = Observer<LocationApp> {
        if (it != null) {
            postQuery(it.toString())
        }
    }

    private fun toggleSaveButton(state: Boolean) {
        sharedViewModel.toggleSaveButton(state)

    }

    private fun postQuery(query: String) {
        sharedViewModel.postQuery(query)
    }

    private val mAdListener = object: AdListener() {
        override fun onAdLoaded() {
        }

        override fun onAdFailedToLoad(adError : LoadAdError) {
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.location.removeObserver(locationObserver)
        sharedViewModel.queryLiveData.removeObserver(queryObserver)
        sharedViewModel.getNetworkLiveData().removeObserver(networkObserver)
        sharedViewModel.stateLiveData.removeObserver(stateObserver)
    }
}