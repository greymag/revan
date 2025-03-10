package net.orangedog.revan.exceptions

import io.ktor.http.*
import net.orangedog.revan.errors.ErrorResponse
import net.orangedog.revan.errors.RevanError

open class RevanException(
    val statusCode: HttpStatusCode,
    private val error: RevanError,
    private val description: String,
    private val retry: Boolean = false,
) : RuntimeException(description) {
    fun createResponse(): ErrorResponse {
        return ErrorResponse(error.toDto(), description, retry)
    }
}