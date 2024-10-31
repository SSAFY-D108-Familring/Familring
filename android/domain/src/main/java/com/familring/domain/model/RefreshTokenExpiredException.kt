package com.familring.domain.model

import java.io.IOException

class RefreshTokenExpiredException(
    val error: ErrorResponse,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : IOException(message)