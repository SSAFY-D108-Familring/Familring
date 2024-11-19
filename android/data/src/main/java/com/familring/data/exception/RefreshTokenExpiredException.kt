package com.familring.data.exception

import com.familring.domain.model.ErrorResponse
import java.io.IOException

class RefreshTokenExpiredException(
    val error: ErrorResponse,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : IOException(message)
