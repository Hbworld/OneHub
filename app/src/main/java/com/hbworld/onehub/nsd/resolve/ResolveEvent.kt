package com.hbworld.onehub.nsd.resolve

import android.net.nsd.NsdServiceInfo

sealed class ResolveEvent {
    data class ServiceResolved(val nsdServiceInfo: NsdServiceInfo) : ResolveEvent()
}