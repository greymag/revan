package net.orangedog.revan.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import net.orangedog.revan.models.trubar.word.SloleksWordRepository
import net.orangedog.revan.modules.trubar.importer.SloleksXmlConverter
import org.koin.ktor.ext.inject

fun Application.trubarModule() {
    val sloleksWordRepository by inject<SloleksWordRepository>()

    routing {
        route("/trubar") {
            get {
                call.respondText("Trubar API")
            }

            get("/test") {
                call.respondText("hellow")
            }
        }
    }
}