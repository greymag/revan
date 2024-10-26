package net.orangedog.revan

import io.ktor.server.application.*
import net.orangedog.revan.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val mongoDBConnectionString = environment.config.propertyOrNull("trubar.mongodb.connectionString")?.getString()
    // TODO: check if the connection string is not null and not empty

    configureAdministration()
    val db = configureDatabases(mongoDBConnectionString!!)
    configureDependencyInjection(db)
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
