package net.orangedog.revan.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
    if (!developmentMode) return

    routing {
        openAPI(path = "openapi")
    }
    routing {
        swaggerUI(path = "swagger")
    }
}
