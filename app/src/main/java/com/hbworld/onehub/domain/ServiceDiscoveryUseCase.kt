package com.hbworld.onehub.domain

import com.hbworld.onehub.nsd.NsdManagerHelper
import com.hbworld.onehub.nsd.discovery.DiscoveryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

interface ServiceDiscoveryUseCase {

    suspend fun searchAllServices(): Flow<DiscoveryEvent>

    suspend fun searchForService(serviceType: String): List<DiscoveryEvent>


    class Impl @Inject constructor(private val nsdManagerHelper: NsdManagerHelper) :
        ServiceDiscoveryUseCase {

        override suspend fun searchAllServices(): Flow<DiscoveryEvent> {
            val discoveryEvents = nsdManagerHelper.startDiscoveryForAll()
                .catch { println("catch error -> $it") }
                .onCompletion {
                    println("onCompletion searchAllServices")
                    println(it)
                }.toList()

            println(discoveryEvents)

            return flow {
                discoveryEvents.forEach { discoveryEvent ->
                    when (discoveryEvent) {
                        is DiscoveryEvent.DiscoveryServiceFound -> {
                            val events = searchForService(
                                nsdManagerHelper.getServiceType(
                                    discoveryEvent.service
                                )
                            )
                            events.forEach { emit(it) }
                        }
                        else -> println("discoveryAll failed")
                    }
                }
            }
        }

        override suspend fun searchForService(serviceType: String): List<DiscoveryEvent> {
            return nsdManagerHelper.startDiscoveryForService(serviceType)
                .catch { println(it) }
                .toList()
        }
    }
}