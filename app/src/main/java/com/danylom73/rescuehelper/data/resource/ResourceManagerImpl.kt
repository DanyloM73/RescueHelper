package com.danylom73.rescuehelper.data.resource

import android.content.Context
import com.danylom73.rescuehelper.domain.resource.ResourceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ResourceManager {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}