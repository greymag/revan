package net.orangedog.revan.plugins

import io.ktor.server.application.*

fun Application.configureAdministration() {
    // TODO: add some security to this and uncomment
//    install(ShutDownUrl.ApplicationCallPlugin) {
//        // The URL that will be intercepted (you can also use the application.conf's ktor.deployment.shutdown.url key)
//        shutDownUrl = "/ktor/application/shutdown"
//        // A function that will be executed to get the exit code of the process
//        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
//    }
}
