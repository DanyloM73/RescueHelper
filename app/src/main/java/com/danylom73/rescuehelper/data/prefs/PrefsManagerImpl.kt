package com.danylom73.rescuehelper.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.danylom73.rescuehelper.core.prefs.dataStore
import com.danylom73.rescuehelper.domain.prefs.PrefsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PrefsManager {

    override suspend fun saveBoolean(key: String, value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(key)] = value
        }
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return context.dataStore.data
            .map { prefs -> prefs[booleanPreferencesKey(key)] ?: defaultValue }
            .first()
    }

    override suspend fun saveString(key: String, value: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getString(key: String, defaultValue: String): String {
        return context.dataStore.data
            .map { prefs -> prefs[stringPreferencesKey(key)] ?: defaultValue }
            .first()
    }

    override suspend fun saveInt(key: String, value: Int) {
        context.dataStore.edit { prefs ->
            prefs[intPreferencesKey(key)] = value
        }
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return context.dataStore.data
            .map { prefs -> prefs[intPreferencesKey(key)] ?: defaultValue }
            .first()
    }

    override suspend fun saveLong(key: String, value: Long) {
        context.dataStore.edit { prefs ->
            prefs[longPreferencesKey(key)] = value
        }
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return context.dataStore.data
            .map { prefs -> prefs[longPreferencesKey(key)] ?: defaultValue }
            .first()
    }

    override suspend fun saveFloat(key: String, value: Float) {
        context.dataStore.edit { prefs ->
            prefs[floatPreferencesKey(key)] = value
        }
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return context.dataStore.data
            .map { prefs -> prefs[floatPreferencesKey(key)] ?: defaultValue }
            .first()
    }
}