package com.danylom73.rescuehelper.di

import android.content.Context
import com.danylom73.rescuehelper.data.alert.AlertControllerImpl
import com.danylom73.rescuehelper.data.flashlight.FlashlightControllerImpl
import com.danylom73.rescuehelper.data.nearby.NearbyRepositoryImpl
import com.danylom73.rescuehelper.domain.alert.AlertController
import com.danylom73.rescuehelper.domain.flashlight.FlashlightController
import com.danylom73.rescuehelper.domain.nearby.NearbyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NearbyModule {

    @Provides
    @Singleton
    fun provideNearbyRepository(
        @ApplicationContext context: Context
    ): NearbyRepository = NearbyRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideFlashlightController(
        @ApplicationContext context: Context
    ): FlashlightController = FlashlightControllerImpl(context)

    @Provides
    @Singleton
    fun provideAlertController(
        @ApplicationContext context: Context
    ): AlertController = AlertControllerImpl(context)
}
