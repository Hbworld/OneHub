package com.hbworld.onehub.di

import com.hbworld.onehub.ui.MyViewModel
import com.hbworld.onehub.util.NetworkConnectivityManager
import com.hbworld.onehub.domain.ResolveServiceUseCase
import com.hbworld.onehub.domain.ServiceDiscoveryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ActivityComponent::class)
class ViewModelModule {

    @ViewModelScoped
    @Provides
    fun providesMyViewModel(
        serviceDiscoveryUseCase: ServiceDiscoveryUseCase,
        resolveServiceUseCase: ResolveServiceUseCase,
        networkConnectivityManager: NetworkConnectivityManager
    ): MyViewModel {
        return MyViewModel(
            serviceDiscoveryUseCase,
            resolveServiceUseCase,
            networkConnectivityManager
        )
    }
}