package com.familring.data.exception

import com.familring.domain.model.ErrorResponse
import java.io.IOException

class ApiException(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val error: ErrorResponse,
) : IOException(message, cause)
