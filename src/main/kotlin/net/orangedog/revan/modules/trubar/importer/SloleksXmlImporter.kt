package net.orangedog.revan.modules.trubar.importer

import io.ktor.util.logging.*
import kotlinx.coroutines.runBlocking
import net.orangedog.revan.repository.trubar.SloleksWordRepository
import java.io.File

class SloleksXmlImporter(
    private val wordRepository: SloleksWordRepository,
    config: SloleksXmlParser.Config = SloleksXmlParser.Config.DEFAULT,
) {
    companion object {
        private val logger = KtorSimpleLogger("revan.trubar.SloleksXmlImporter")

        private const val FILE_PREFIX = "sloleks_3.0_"
        private const val FILE_EXTENSION = ".xml"
    }

    private val parser = SloleksXmlParser(config)

    fun importAllFromDirectory(directory: File) {
        logger.info("Importing all xml files from directory: ${directory.absolutePath}")

        val files = directory.listFiles()
            ?.filter { it.isFile && it.name.endsWith(FILE_EXTENSION) && it.name.startsWith(FILE_PREFIX) }
            ?.sortedBy { it.name }

        if (files?.isNotEmpty() == true) {
            logger.info("Found ${files.size} files for import in directory: ${directory.absolutePath}")

            files.forEach {
                try {
                    importFile(it)
                } catch (e: Exception) {
                    logger.error("Error importing file: ${it.absolutePath}", e)
                    return@importAllFromDirectory
                }
            }

            logger.info("Import completed successfully")
        } else {
            logger.warn("No files for import found in directory: ${directory.absolutePath}")
        }
    }

    fun importFile(file: File) {
        logger.info("Importing file: ${file.absolutePath}")

        var imported = 0
        parser.parse(file) {
            runBlocking {
                logger.trace("Importing word: ${it.lemma}")
                // TODO: for optimization, we should batch these up
                wordRepository.replaceOne(it)
                imported++
            }
        }

        logger.info("Imported $imported words")
    }
}