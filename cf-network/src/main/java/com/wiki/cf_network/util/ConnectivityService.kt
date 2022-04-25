package com.wiki.cf_network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager

interface ConnectivityService {
    fun isOnline(): Boolean
    fun isOffline(): Boolean
}

class ConnectivityServiceImpl(
    context: Context
) : ConnectivityService {
    private var wifiManager: WifiManager? = null
    private var connectivityManager: ConnectivityManager? = null

    init {
        wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun isOnline(): Boolean {
        val capabilities = connectivityManager?.getNetworkCapabilities(connectivityManager?.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    override fun isOffline(): Boolean {
        return !isOnline()
    }

}