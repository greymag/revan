package net.orangedog.revan.repository.trubar

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.util.logging.*
import kotlinx.coroutines.flow.firstOrNull
import net.orangedog.revan.models.trubar.word.SloleksWord
import net.orangedog.revan.plugins.sloleksWords
import org.bson.types.ObjectId

interface SloleksWordRepository {
    suspend fun insertOne(word: SloleksWord)
    suspend fun replaceOne(word: SloleksWord)
    suspend fun findOneById(objectId: ObjectId): SloleksWord?
}

class SloleksWordRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : SloleksWordRepository {
    companion object {
        private val logger = KtorSimpleLogger("revan.trubar.SloleksWordRepository")
    }

    override suspend fun insertOne(word: SloleksWord): Unit = request {
        insertOne(word)
    }

    override suspend fun replaceOne(word: SloleksWord): Unit = request {
        getCollection().replaceOne(
            Filters.eq("lemma", word.lemma),
            word,
            ReplaceOptions().upsert(true)
        )
    }

    override suspend fun findOneById(objectId: ObjectId): SloleksWord? = request {
        withDocumentClass<SloleksWord>()
            .find(Filters.eq("_id", objectId))
            .firstOrNull()
    }

    private suspend fun <T> request(impl: suspend MongoCollection<SloleksWord>.() -> T): T = try {
        getCollection().impl()
    } catch (e: MongoException) {
        logger.error(e)
        throw e
    }

    private fun getCollection() = mongoDatabase.sloleksWords
}