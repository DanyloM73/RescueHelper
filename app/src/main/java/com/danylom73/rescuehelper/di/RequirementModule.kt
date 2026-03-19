package com.danylom73.rescuehelper.di

import android.content.Context
import com.danylom73.rescuehelper.data.requirement.RequirementRepositoryImpl
import com.danylom73.rescuehelper.domain.requirement.RequirementRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RequirementModule {

    @Provides
    @Singleton
    fun provideRequirementRepository(
        @ApplicationContext context: Context
    ): RequirementRepository = RequirementRepositoryImpl(context)
}