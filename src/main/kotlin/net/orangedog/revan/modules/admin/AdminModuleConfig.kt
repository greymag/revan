package net.orangedog.revan.modules.admin

import io.ktor.server.config.*

private const val ADMIN_USERNAME = "admin.username"
private const val ADMIN_PASSWORD = "admin.password"

data class AdminModuleConfig(
    val adminUsername: String,
    val adminPassword: String,
) {
    companion object {
        fun from(config: ApplicationConfig): AdminModuleConfig {
            return AdminModuleConfig(
                adminUsername = config.propertyOrNull(ADMIN_USERNAME)?.getString() ?: throw  IllegalArgumentException("$ADMIN_USERNAME is required in the configuration"),
                adminPassword = config.propertyOrNull(ADMIN_PASSWORD)?.getString() ?: throw IllegalArgumentException("$ADMIN_PASSWORD is required in the configuration"),
            )
        }
    }
}