package com.desuzed.clocknweather.mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkLiveData(val context: Context) : LiveData<Boolean>(true) {
    private val TAG = "TAG"
    private val networkManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val validNetworks: MutableSet<Network> = HashSet()


    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }


    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        networkManager.registerNetworkCallback(networkRequest, networkCallback)

    }


    override fun onInactive() {

        networkManager.unregisterNetworkCallback(networkCallback)
    }


    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        @SuppressLint("MissingPermission")
        override fun onAvailable(network: Network) {
            Log.i(TAG, "onAvailable: $network")
            val networkCapabilities = networkManager.getNetworkCapabilities(network)
            val isInternet =
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            Log.i(TAG, "onAvailable: $network \n $isInternet")
            if (isInternet == true) {
                validNetworks.add(network)
            }
            checkValidNetworks()

        }

        override fun onLost(network: Network) {
            Log.i(TAG, "onLost: $network")
            validNetworks.remove(network)
            checkValidNetworks()
        }


    }
}