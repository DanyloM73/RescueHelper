package com.danylom73.rescuehelper.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.danylom73.rescuehelper.data.prefs.PrefsManagerImpl
import com.danylom73.rescuehelper.domain.prefs.PrefsManager
import com.danylom73.rescuehelper.util.PREFS_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(PREFS_NAME)
            }
        )
    }

    @Provides
    @Singleton
    fun providePrefsManager(
        @ApplicationContext context: Context
    ): PrefsManager = PrefsManagerImpl(context)
}