package com.danylom73.rescuehelper.domain.prefs

interface PrefsManager {
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    suspend fun saveString(key: String, value: String)
    suspend fun getString(key: String, defaultValue: String = ""): String

    suspend fun saveInt(key: String, value: Int)
    suspend fun getInt(key: String, defaultValue: Int = 0): Int

    suspend fun saveLong(key: String, value: Long)
    suspend fun getLong(key: String, defaultValue: Long = 0L): Long

    suspend fun saveFloat(key: String, value: Float)
    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float
}