package com.desuzed.everyweather.view.fragments

import android.os.Bundle
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
import com.desuzed.everyweather.model.model.LocationApp
import com.desuzed.everyweather.model.vm.AppViewModelFactory
import com.desuzed.everyweather.model.vm.SharedViewModel
import com.desuzed.everyweather.view.StateUI
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError


class MainFragment : Fragment() {
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var tvInternetConnection: TextView
    private lateinit var mAdView : AdView
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
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        observeLiveData()
        tryLoadAd()
    }

    private fun tryLoadAd (){
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun bind() {
        tvInternetConnection = fragmentMainBinding.tvInternetConnection
        mAdView = fragmentMainBinding.adView
        mAdView.adListener = mAdListener
    }

    private fun observeLiveData() {
        sharedViewModel.loadCachedQuery()
        sharedViewModel.location.observe(viewLifecycleOwner, locationObserver)
        sharedViewModel.getNetworkLiveData().observe(viewLifecycleOwner, networkObserver)
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
            postQuery(it.toString())
        }
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
        sharedViewModel.getNetworkLiveData().removeObserver(networkObserver)
    }
}