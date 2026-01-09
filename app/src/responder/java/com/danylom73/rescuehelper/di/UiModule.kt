package com.danylom73.rescuehelper.di

import com.danylom73.rescuehelper.presentation.screen.config.NearbyScreenUiConfig
import com.danylom73.rescuehelper.ui.ResponderNearbyScreenUiConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UiModule {

    @Binds
    abstract fun bindUiConfig(
        impl: ResponderNearbyScreenUiConfig
    ): NearbyScreenUiConfig
}
