package net.orangedog.revan.repository.trubar

import com.mongodb.MongoException
import com.mongodb.client.model.*
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.util.logging.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import net.orangedog.revan.models.trubar.word.SloleksFindResultEntry
import net.orangedog.revan.models.trubar.word.SloleksWord
import net.orangedog.revan.plugins.sloleksWords
import org.bson.types.ObjectId

interface SloleksWordRepository {
    suspend fun insertOne(word: SloleksWord)
    suspend fun replaceOne(word: SloleksWord)
    suspend fun findOneById(objectId: ObjectId): SloleksWord?
    suspend fun findOneByLemma(lemma: String): SloleksWord?
    suspend fun findStartsWith(str: String): List<SloleksFindResultEntry>
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
            Filters.eq(SloleksWord::lemma.name, word.lemma),
            word,
            ReplaceOptions().upsert(true)
        )
    }

    override suspend fun findOneById(objectId: ObjectId): SloleksWord? = request {
        withDocumentClass<SloleksWord>()
            .find(Filters.eq("_id", objectId))
            .firstOrNull()
    }

    override suspend fun findOneByLemma(lemma: String): SloleksWord? = request {
        withDocumentClass<SloleksWord>()
            .find(Filters.eq(SloleksWord::lemma.name, lemma))
            .firstOrNull()
    }

    override suspend fun findStartsWith(str: String): List<SloleksFindResultEntry> = request {
        find<SloleksFindResultEntry>(Filters.regex(SloleksWord::lemma.name, "^${Regex.escape(str)}", "i"))
            .projection(Projections.include(SloleksWord::lemma.name, SloleksWord::category.name))
            .collation(Collation.builder().locale("sl").caseLevel(false).build())
            .sort(Sorts.ascending(SloleksWord::lemma.name))
            .toList()
    }

    private suspend fun <T> request(impl: suspend MongoCollection<SloleksWord>.() -> T): T = try {
        getCollection().impl()
    } catch (e: MongoException) {
        logger.error(e)
        throw e
    }

    private fun getCollection() = mongoDatabase.sloleksWords
}