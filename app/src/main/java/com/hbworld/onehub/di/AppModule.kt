package com.hbworld.onehub.di

import android.content.Context
import android.net.nsd.NsdManager
import com.hbworld.onehub.util.NetworkConnectivityManager
import com.hbworld.onehub.domain.ResolveServiceUseCase
import com.hbworld.onehub.nsd.NsdManagerHelper
import com.hbworld.onehub.domain.ServiceDiscoveryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesNetworkConnectivityManager(@ApplicationContext appContext: Context): NetworkConnectivityManager {
        return NetworkConnectivityManager.Impl(appContext)
    }

    @Provides
    fun providesNsdManager(@ApplicationContext appContext: Context): NsdManager {
        return appContext.getSystemService(Context.NSD_SERVICE) as NsdManager
    }

    @Provides
    fun providesNsdManagerHelper(nsdManager: NsdManager): NsdManagerHelper {
        return NsdManagerHelper.Impl(nsdManager)
    }

    @Provides
    fun providesServiceDiscoveryUseCase(nsdManagerHelper: NsdManagerHelper): ServiceDiscoveryUseCase {
        return ServiceDiscoveryUseCase.Impl(nsdManagerHelper)
    }

    @Provides
    fun providesResolveUseCase(nsdManagerHelper: NsdManagerHelper): ResolveServiceUseCase {
        return ResolveServiceUseCase.Impl(nsdManagerHelper)
    }


}