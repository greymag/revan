package net.orangedog.revan

import io.ktor.server.application.*
import net.orangedog.revan.modules.admin.AdminModuleConfig
import net.orangedog.revan.modules.trubar.TrubarModuleConfig
import net.orangedog.revan.plugins.*

private const val CONFIG_MONGODB_CONNECTION_STRING = "mongodb.connectionString"

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val mongoDBConnectionString = environment.config.propertyOrNull(CONFIG_MONGODB_CONNECTION_STRING)?.getString()
    if (mongoDBConnectionString.isNullOrEmpty()) {
        throw IllegalArgumentException("MongoDB connection string is not set. " +
                "Set parameter $CONFIG_MONGODB_CONNECTION_STRING in the configuration file.")
    }

    val adminConfig = AdminModuleConfig.from(environment.config)
    val trubarConfig = TrubarModuleConfig.from(environment.config)

    configureAdministration()
    val db = configureDatabases(mongoDBConnectionString)
    configureDependencyInjection(db, trubarConfig)
    configureAuthentication(adminConfig)
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureExceptionHandling()
    configureRouting()
}
