package com.desuzed.everyweather.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.desuzed.everyweather.App
import com.desuzed.everyweather.databinding.FragmentMainBinding
import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.vm.AppViewModelFactory
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
import com.desuzed.everyweather.ui.StateRequest
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError


class MainFragment : Fragment() {
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvInternetConnection: TextView
    private lateinit var mAdView : AdView
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
        swipeRefresh.setOnRefreshListener {
            sharedViewModel.queryLiveData.value?.let { getQueryForecast(it)
            }
        }
        tryLoadAd()
    }

    private fun tryLoadAd (){
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun bind() {
        tvInternetConnection = fragmentMainBinding.tvInternetConnection
        swipeRefresh = fragmentMainBinding.swipeRefresh
        mAdView = fragmentMainBinding.adView
        mAdView.adListener = mAdListener
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


    private fun launchRefresh(state: Boolean) {
        swipeRefresh.isRefreshing = state
    }

    private fun toast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        )
            .show()
    }

    private val stateObserver = Observer<StateRequest> {
        when (it) {
            is StateRequest.Loading -> {
                launchRefresh(true)
            }
            is StateRequest.Success -> {
                onSuccess(it)
            }
            is StateRequest.Error -> {
                onError(it)
            }
            is StateRequest.NoData -> {
                launchRefresh(false)
                toggleSaveButton(false)
            }
        }
    }

    private fun onSuccess(it: StateRequest.Success) {
        if (it.toggleSaveButton) toggleSaveButton(true)
        else toggleSaveButton(false)
        if (it.message.isNotEmpty()) {
            toast(it.message)
        }
        launchRefresh(false)
    }

    private fun onError(it: StateRequest.Error) {
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
                tvInternetConnection.visibility = View.GONE
                tryLoadAd()
            }
            else -> {
                tvInternetConnection.visibility = View.VISIBLE
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
            Log.d("ad", "onAdLoaded")
        }

        override fun onAdFailedToLoad(adError : LoadAdError) {
            Log.d("ad", "onAdFailedToLoad")
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