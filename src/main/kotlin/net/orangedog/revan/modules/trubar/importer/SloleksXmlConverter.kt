package net.orangedog.revan.modules.trubar.importer

import io.ktor.util.logging.*
import net.orangedog.revan.models.trubar.word.SloleksWord
import java.io.File

class SloleksXmlConverter(config: SloleksXmlParser.Config = SloleksXmlParser.Config.DEFAULT) {
    companion object {
        private val logger = KtorSimpleLogger("revan.trubar.SloleksXmlConverter")
    }

    private val parser = SloleksXmlParser(config)

    fun loadAndConvert(file: File): List<SloleksWord> {
        logger.info("Loading and converting file: ${file.absolutePath}")

        val result = mutableListOf<SloleksWord>()
        parser.parse(file) {
            result.add(it)
        }

        logger.info("Loaded and converted ${result.size} words")
        return result
    }
}
