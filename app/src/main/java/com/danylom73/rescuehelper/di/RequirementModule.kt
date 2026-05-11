package com.danylom73.rescuehelper.di

import android.content.Context
import com.danylom73.rescuehelper.data.requirement.RequirementMonitorImpl
import com.danylom73.rescuehelper.data.requirement.RequirementRepositoryImpl
import com.danylom73.rescuehelper.domain.requirement.RequirementMonitor
import com.danylom73.rescuehelper.domain.requirement.RequirementRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RequirementModule {

    @Provides
    @Singleton
    fun provideRequirementRepository(
        @ApplicationContext context: Context
    ): RequirementRepository = RequirementRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideRequirementMonitor(
        requirementRepository: RequirementRepository,
        @ApplicationScope appScope: CoroutineScope
    ): RequirementMonitor = RequirementMonitorImpl(requirementRepository, appScope)
}