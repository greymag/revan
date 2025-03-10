package net.orangedog.revan.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.util.logging.*
import net.orangedog.revan.errors.CommonError
import net.orangedog.revan.errors.ErrorResponse
import net.orangedog.revan.exceptions.RevanException

fun Application.configureExceptionHandling() {
    val logger = KtorSimpleLogger("revan.common.ExceptionHandling")
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            logger.error("Unexpected exception", cause)

            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    CommonError.System.toDto(),
                    "Internal server error",
                    false
                )
            )
        }

        exception<RevanException> { call, cause ->
            call.respond(
                cause.statusCode,
                cause.createResponse()
            )
        }
    }
}
