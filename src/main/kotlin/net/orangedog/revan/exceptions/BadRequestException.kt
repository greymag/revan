package net.orangedog.revan.exceptions

import io.ktor.http.*
import net.orangedog.revan.errors.CommonError

class BadRequestException(description: String) : RevanException(
    statusCode = HttpStatusCode.BadRequest,
    error = CommonError.BadRequest,
    description = description,
    retry = false,
)