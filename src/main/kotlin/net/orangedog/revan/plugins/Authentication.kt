package net.orangedog.revan.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.util.*
import net.orangedog.revan.modules.admin.AdminModuleConfig

fun Application.configureAuthentication(
    adminModuleConfig: AdminModuleConfig,
) {
    val adminUserTable = UserHashedTableAuth(
        table = mapOf(
            adminModuleConfig.adminUsername to adminModuleConfig.adminPassword.decodeBase64Bytes()
        ),
        digester = adminAuthDigestFunction
    )

    install(Authentication) {
        basic("admin") {
            realm = "Access to the '/admin' path"
            validate { credentials ->
                adminUserTable.authenticate(credentials)
            }
        }
    }
}

val adminAuthDigestFunction = getDigestFunction("SHA-256") { "revan${it.length}" }