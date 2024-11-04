package com.familring.data.di

import com.familring.data.repositoryImpl.AuthRepositoryImpl
import com.familring.data.repositoryImpl.FamilyRepositoryImpl
import com.familring.data.repositoryImpl.UserRepositoryImpl
import com.familring.domain.repository.AuthRepository
import com.familring.domain.repository.FamilyRepository
import com.familring.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindFamilyRepository(familyRepositoryImpl: FamilyRepositoryImpl): FamilyRepository
}
