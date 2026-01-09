package com.danylom73.rescuehelper.di

import com.danylom73.rescuehelper.core.role.RoleProvider
import com.danylom73.rescuehelper.role.RoleProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RoleModule {

    @Binds
    abstract fun bindRoleProvider(
        impl: RoleProviderImpl
    ): RoleProvider
}
