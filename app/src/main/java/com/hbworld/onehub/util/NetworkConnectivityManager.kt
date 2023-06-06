package com.hbworld.onehub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

interface NetworkConnectivityManager {
    fun isConnected(): Boolean

    class Impl(private val context: Context) : NetworkConnectivityManager {

        override fun isConnected(): Boolean {
            var isConnected = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                isConnected =
                    activeNetwork.hasWifiTransport() || activeNetwork.hasEthernetTransport()
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    isConnected = when (type) {
                        ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_ETHERNET, ConnectivityManager.TYPE_BLUETOOTH -> true
                        else -> false
                    }
                }
            }
            return isConnected
        }

        private fun NetworkCapabilities.hasWifiTransport() =
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

        private fun NetworkCapabilities.hasEthernetTransport() =
            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}