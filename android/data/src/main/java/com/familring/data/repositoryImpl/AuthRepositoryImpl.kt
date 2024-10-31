package com.familring.data.repositoryImpl

import com.familring.data.network.api.AuthApi
import com.familring.domain.datasource.AuthDataSource
import com.familring.domain.datasource.TokenDataSource
import com.familring.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val api: AuthApi,
        private val tokenDataSource: TokenDataSource,
        private val authDataSource: AuthDataSource,
    ) : AuthRepository
