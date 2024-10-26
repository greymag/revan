package net.orangedog.revan.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.orangedog.revan.models.trubar.SloleksWord
import net.orangedog.revan.models.trubar.SloleksWordRepository
import org.bson.types.ObjectId
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