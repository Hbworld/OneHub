package com.hbworld.onehub.nsd

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.hbworld.onehub.nsd.discovery.DiscoveryEvent
import com.hbworld.onehub.nsd.discovery.DiscoveryListenerFlow
import com.hbworld.onehub.nsd.resolve.ResolveEvent
import com.hbworld.onehub.nsd.resolve.ResolveListenerFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface NsdManagerHelper {


    fun startDiscoveryForAll(): Flow<DiscoveryEvent>

    fun startDiscoveryForService(serviceType : String): Flow<DiscoveryEvent>

    fun resolveService(nsdServiceInfo: NsdServiceInfo) : Flow<ResolveEvent>

    fun getServiceType(serviceInfo: NsdServiceInfo): String


    class Impl @Inject constructor(private val nsdManager: NsdManager) : NsdManagerHelper {

        companion object {
            const val PRIMARY_SERVICE_TYPE = "_services._dns-sd._udp"
            const val PROTOCOL_TYPE: Int = NsdManager.PROTOCOL_DNS_SD


        }

        override fun startDiscoveryForAll() = callbackFlow {
            val listener = DiscoveryListenerFlow(this)
            nsdManager.discoverServices(
                PRIMARY_SERVICE_TYPE,
                PROTOCOL_TYPE,
                listener
            )
            delay(1000)
            nsdManager.stopServiceDiscovery(listener)
            awaitClose{
                println("closing discoveryAll channel")
                channel.close()
            }
        }

        override fun startDiscoveryForService(serviceType : String) = callbackFlow {
            val listener = DiscoveryListenerFlow(this)
            nsdManager.discoverServices(
                serviceType,
                PROTOCOL_TYPE,
                listener
            )
            delay(500)
            nsdManager.stopServiceDiscovery(listener)
            awaitClose{
                println("closing service discovery channel")
                channel.close()
            }
        }

        override fun resolveService(nsdServiceInfo: NsdServiceInfo) = callbackFlow{
            val listener = ResolveListenerFlow(this)
            nsdManager.resolveService(
                nsdServiceInfo,
                listener
            )
            delay(500)
            channel.close()
            awaitClose{
                println("closing resolveService channel")
            }
        }

        override fun getServiceType(serviceInfo: NsdServiceInfo): String {
            return serviceInfo.serviceName + if (serviceInfo.serviceType.contains("tcp")) "._tcp"
            else "._udp"
        }


    }
}