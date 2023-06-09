package com.hbworld.onehub.ui

import android.net.nsd.NsdServiceInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbworld.onehub.dtos.Device
import com.hbworld.onehub.util.NetworkConnectivityManager
import com.hbworld.onehub.dtos.Results
import com.hbworld.onehub.domain.ResolveServiceUseCase
import com.hbworld.onehub.domain.ServiceDiscoveryUseCase
import com.hbworld.onehub.nsd.discovery.DiscoveryEvent
import com.hbworld.onehub.nsd.resolve.ResolveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val serviceDiscoveryUseCase: ServiceDiscoveryUseCase,
    private val resolveServiceUseCase: ResolveServiceUseCase,
    private val networkConnectivityManager: NetworkConnectivityManager
) :
    ViewModel() {

    private val deviceList = mutableListOf<Device>()

    private val _devices = MutableStateFlow<Results>(Results.loading)
    val devices: StateFlow<Results> get() = _devices

    companion object {
        const val TAG = "MyViewModel"
    }

    fun readARP(){
        ARP.Impl().readARPCache()
    }


    fun searchForAvailableServices() {
        readARP()
        _devices.value = Results.loading
        if (networkConnectivityManager.isConnected()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    serviceDiscoveryUseCase.searchAllServices()
                        .catch {  _devices.value = Results.error("Please try again something went wrong") }
                        .onCompletion {
                            println("searchForAvailableServices onCompletion")
                            _devices.value = Results.success(deviceList)
                        }
                        .collect {
                            if (it is DiscoveryEvent.DiscoveryServiceFound)
                                resolveService(it.service)
                        }
                }
            }
        } else {
           _devices.value = Results.error("Please connect to a Network")
        }

    }

    private suspend fun resolveService(nsdServiceInfo: NsdServiceInfo) {
        resolveServiceUseCase.resolveService(nsdServiceInfo)
            .onCompletion {
                println("resolveService onCompletion")
            }
            .collect { resolvedEvent ->
                println("resolve - $resolvedEvent")
                if (resolvedEvent is ResolveEvent.ServiceResolved) {

                    deviceList.find { it.ipAddress == resolvedEvent.nsdServiceInfo.host }.apply {
                        if (this != null) {
                            this.serviceTypes.add(resolvedEvent.nsdServiceInfo.serviceType)
                            this.openPorts.add(resolvedEvent.nsdServiceInfo.port)
                            when (resolvedEvent.nsdServiceInfo.serviceType) {
                                "._companion-link._tcp", "._airplay._tcp", "._meshcop._udp", "._androidtvremote2._tcp" -> this.name =
                                    resolvedEvent.nsdServiceInfo.serviceName
                            }
                        } else {
                            deviceList.add(
                                Device(
                                    ipAddress = resolvedEvent.nsdServiceInfo.host,
                                    name = resolvedEvent.nsdServiceInfo.serviceName,
                                    serviceTypes = mutableSetOf(resolvedEvent.nsdServiceInfo.serviceType),
                                    openPorts = mutableSetOf(resolvedEvent.nsdServiceInfo.port),
                                )
                            )
                        }
                    }


                }


            }
        println(deviceList.size)
        deviceList.forEach {
            println(it)

        }
    }


}