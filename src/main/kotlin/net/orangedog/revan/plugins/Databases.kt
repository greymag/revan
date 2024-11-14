package net.orangedog.revan.plugins

import com.mongodb.client.model.IndexOptions
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import io.ktor.util.logging.*
import kotlinx.coroutines.*
import net.orangedog.revan.models.trubar.word.SloleksWord
import org.bson.Document

private val logger = KtorSimpleLogger("revan.Database")

fun Application.configureDatabases(connectionString: String): MongoDatabase {
    logger.info("Configuring database")

    val mongoClient = MongoClient.create(connectionString)
    val database = mongoClient.getDatabase("revan")

    runBlocking {
        createIndexes(database)
    }

    logger.info("Configuration completed")
    return database
}

private suspend fun createIndexes(database: MongoDatabase) {
    logger.info("Creating indexes")

    database.sloleksWords.apply {
        createIndex(Document("lemma", 1), IndexOptions().unique(true))
    }
}

enum class RevanCollection(val collectionName: String) {
    SLOLEKS_WORDS("trubar_sloleks_words")
}

private inline fun <reified T : Any> MongoDatabase.getCollection(collection: RevanCollection) =
    getCollection<T>(collection.collectionName)

val MongoDatabase.sloleksWords get() = getCollection<SloleksWord>(RevanCollection.SLOLEKS_WORDS)