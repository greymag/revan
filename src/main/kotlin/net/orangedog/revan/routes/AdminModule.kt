package net.orangedog.revan.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import net.orangedog.revan.modules.trubar.TrubarModuleConfig
import net.orangedog.revan.modules.trubar.importer.SloleksXmlImporter
import net.orangedog.revan.plugins.adminAuthDigestFunction
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.scope
import java.io.File


fun Application.adminModule() {
    val trubarConfig by inject<TrubarModuleConfig>()

    routing {
        route("/admin") {
            get {
                call.respondText("These aren't the droids you're looking for")
            }

            get("/pass-digest") {
                if (!application.developmentMode) {
                    call.respondText("This endpoint is only available in development mode", status = HttpStatusCode.Forbidden)
                    return@get
                }

                val pass = call.request.queryParameters["pass"]
                if (pass?.isNotEmpty() == true) {
                    call.respondText(adminAuthDigestFunction(pass).encodeBase64())
                } else {
                    call.respondText("'pass' query parameter is required", status = HttpStatusCode.BadRequest)
                }
            }

            authenticate("admin") {
                get("/import-words") {
                    val dirPath = trubarConfig.importWordsDir
                    if (dirPath?.isNotEmpty() != true){
                        call.respondText("Import directory is not set")
                    } else {
                        val dir = File(dirPath)

                        when {
                            dir.exists().not() -> call.respondText("Directory $dirPath does not exist")
                            dir.isDirectory.not() -> call.respondText("$dirPath is not a directory")
                            else -> {
                                val importer = call.scope.get<SloleksXmlImporter>()
                                importer.importAllFromDirectory(dir)
                                call.respondText("Import completed")
                            }
                        }
                    }
                }
            }
        }
    }
}