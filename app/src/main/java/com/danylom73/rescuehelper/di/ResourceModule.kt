package com.danylom73.rescuehelper.di

import android.content.Context
import com.danylom73.rescuehelper.data.resource.ResourceManagerImpl
import com.danylom73.rescuehelper.domain.resource.ResourceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {

    @Provides
    @Singleton
    fun provideResourceManager(
        @ApplicationContext context: Context
    ): ResourceManager = ResourceManagerImpl(context)
}