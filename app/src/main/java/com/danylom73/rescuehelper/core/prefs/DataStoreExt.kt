package com.danylom73.rescuehelper.core.prefs

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.danylom73.rescuehelper.util.PREFS_NAME

val Context.dataStore by preferencesDataStore(
    name = PREFS_NAME
)