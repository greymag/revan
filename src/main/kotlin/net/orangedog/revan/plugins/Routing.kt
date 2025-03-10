package net.orangedog.revan.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.orangedog.revan.routes.adminModule
import net.orangedog.revan.routes.trubarModule

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("I am Revan reborn. And before me you are nothing.")
        }
    }

    adminModule()
    trubarModule()
}
