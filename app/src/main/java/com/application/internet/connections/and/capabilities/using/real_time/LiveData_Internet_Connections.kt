package com.application.internet.connections.and.capabilities.using.real_time

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class LiveDataInternetConnections(private val connectivityManager: ConnectivityManager):
LiveData<Boolean>(){

    constructor(appContext: Application) : this(
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback(){

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(ContentValues.TAG, "onAvailable: Network ${network} is Available")
            postValue(true)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities) {
            val isInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            Log.d(ContentValues.TAG, "networkCapabilities: ${network} $networkCapabilities")
            val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            if (isValidated){
                Log.d(ContentValues.TAG, "hasCapability: ${network} $networkCapabilities")
            } else{
                Log.d(ContentValues.TAG, "Network has No Connection Capability: ${network} $networkCapabilities")
            }
            postValue(isInternet && isValidated)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d(ContentValues.TAG, "onLost: ${network} Network Lost")
            postValue(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build(), networkCallback)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}