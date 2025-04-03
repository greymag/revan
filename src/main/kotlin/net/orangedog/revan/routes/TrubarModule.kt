package net.orangedog.revan.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.orangedog.revan.exceptions.BadRequestException
import net.orangedog.revan.models.trubar.api.FindResultDto
import net.orangedog.revan.models.trubar.api.WordDataResultDto
import net.orangedog.revan.repository.trubar.SloleksWordRepository
import org.koin.ktor.ext.inject

fun Application.trubarModule() {
    val sloleksWordRepository by inject<SloleksWordRepository>()

    routing {
        route("/trubar") {
            get {
                call.respondText("Trubar API")
            }

            get("/find") {
                val search = call.request.queryParameters["search"]
                // validate search
                if (search.isNullOrEmpty()) {
                    throw BadRequestException("Search parameter is required")
                }

                val maxCount = 30
                val list = sloleksWordRepository.findStartsWith(search, maxCount)
                call.respond(FindResultDto(list))
            }

            get("/wordData") {
                val lemma = call.parameters["lemma"]
                if (lemma.isNullOrEmpty()) {
                    throw BadRequestException("Lemma parameter is required")
                }

                val word = sloleksWordRepository.findOneByLemma(lemma)
                call.respond(WordDataResultDto(word))
            }
        }
    }
}