package net.orangedog.revan

import io.ktor.server.application.*
import net.orangedog.revan.modules.admin.AdminModuleConfig
import net.orangedog.revan.modules.trubar.TrubarModuleConfig
import net.orangedog.revan.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val mongoDBConnectionString = environment.config.propertyOrNull("mongodb.connectionString")?.getString()
    // TODO: check if the connection string is not null and not empty

    val adminConfig = AdminModuleConfig.from(environment.config)
    val trubarConfig = TrubarModuleConfig.from(environment.config)

    configureAdministration()
    val db = configureDatabases(mongoDBConnectionString!!)
    configureDependencyInjection(db, trubarConfig)
    configureAuthentication(adminConfig)
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureExceptionHandling()
    configureRouting()
}
