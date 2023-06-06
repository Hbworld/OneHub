package com.hbworld.onehub.nsd.discovery

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.hbworld.onehub.nsd.DiscoveryStartFailed
import com.hbworld.onehub.nsd.DiscoveryStopFailed
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking

internal data class DiscoveryListenerFlow(
    private val producerScope: ProducerScope<DiscoveryEvent>,
) : NsdManager.DiscoveryListener {

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        producerScope.trySendBlocking(DiscoveryEvent.DiscoveryServiceFound(service = serviceInfo))
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        producerScope.channel.close(DiscoveryStopFailed(serviceType, errorCode))
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        producerScope.channel.close(DiscoveryStartFailed(serviceType, errorCode))
    }

    override fun onDiscoveryStarted(serviceType: String) {
        println("onDiscoveryStarted $serviceType")
    }

    override fun onDiscoveryStopped(serviceType: String) {
        println("onDiscoveryStopped $serviceType")
        producerScope.channel.close()
    }

    override fun onServiceLost(service: NsdServiceInfo) {
        producerScope.trySendBlocking(DiscoveryEvent.DiscoveryServiceLost(service = service))
    }

}