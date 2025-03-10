package net.orangedog.revan.errors

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: ErrorDto,
    val description: String? = null,
    val retry: Boolean = false,
)

@Serializable
data class ErrorDto(
    val domain: String,
    val code: Int,
)