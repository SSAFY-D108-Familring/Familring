package com.familring.presentation.screen.signup

import com.familring.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
){

}