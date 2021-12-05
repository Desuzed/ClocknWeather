package com.desuzed.everyweather.mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkLiveData(val context: Context) : LiveData<Boolean>(true) {
    private val networkManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val validNetworks: MutableSet<Network> = HashSet()


    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

//TODO Problems with permissions and android studio bug
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
            val networkCapabilities = networkManager.getNetworkCapabilities(network)
            val isInternet =
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (isInternet == true) {
                validNetworks.add(network)
            }
            checkValidNetworks()

        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }


    }
}