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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindFamilyRepository(familyRepositoryImpl: FamilyRepositoryImpl): FamilyRepository
}