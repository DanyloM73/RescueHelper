package com.danylom73.rescuehelper.role

import com.danylom73.rescuehelper.core.role.AppRole
import com.danylom73.rescuehelper.core.role.RoleProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleProviderImpl @Inject constructor() : RoleProvider {
    override val role = AppRole.USER
}
