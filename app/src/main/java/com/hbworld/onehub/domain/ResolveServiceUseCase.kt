package com.hbworld.onehub.domain

import android.net.nsd.NsdServiceInfo
import com.hbworld.onehub.nsd.NsdManagerHelper
import com.hbworld.onehub.nsd.resolve.ResolveEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ResolveServiceUseCase {

    suspend fun resolveService(nsdServiceInfo: NsdServiceInfo): Flow<ResolveEvent>


    class Impl @Inject constructor(private val nsdManagerHelper: NsdManagerHelper) :
        ResolveServiceUseCase {

        override suspend fun resolveService(nsdServiceInfo: NsdServiceInfo): Flow<ResolveEvent> {
            return nsdManagerHelper.resolveService(nsdServiceInfo)
        }
    }
}