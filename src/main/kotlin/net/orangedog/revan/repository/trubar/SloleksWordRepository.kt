package net.orangedog.revan.repository.trubar

import com.mongodb.MongoException
import com.mongodb.client.model.Aggregates.*
import com.mongodb.client.model.Collation
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.*
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.util.logging.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import net.orangedog.revan.models.trubar.word.SloleksFindResultEntry
import net.orangedog.revan.models.trubar.word.SloleksWord
import net.orangedog.revan.plugins.sloleksWords
import org.bson.Document
import org.bson.types.ObjectId

interface SloleksWordRepository {
    suspend fun insertOne(word: SloleksWord)
    suspend fun replaceOne(word: SloleksWord)
    suspend fun findOneById(objectId: ObjectId): SloleksWord?
    suspend fun findOneByLemma(lemma: String): SloleksWord?
    suspend fun findStartsWith(str: String, limit: Int): List<SloleksFindResultEntry>
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

    override suspend fun findStartsWith(str: String, limit: Int): List<SloleksFindResultEntry> = request {
        val pattern = prepareSearchRegex(str)
        aggregate<SloleksFindResultEntry>(
            listOf(
                match(Filters.regex(SloleksWord::lemma.name, pattern, "i")),
                project(
                    fields(
                        include(SloleksWord::lemma.name, SloleksWord::category.name),
                        computed(
                            "lemmaLength",
                            Document("\$strLenCP", "\$${SloleksWord::lemma.name}")
                        )
                    )
                ),
                sort(
                    orderBy(
                        ascending("lemmaLength"),
                        ascending(SloleksWord::lemma.name)
                    )
                ),
                limit(limit)
            )
        )
            .collation(Collation.builder().locale("sl").caseLevel(false).build())
            .toList()
    }

    private suspend fun <T> request(impl: suspend MongoCollection<SloleksWord>.() -> T): T = try {
        getCollection().impl()
    } catch (e: MongoException) {
        logger.error(e)
        throw e
    }

    private fun getCollection() = mongoDatabase.sloleksWords

    private fun prepareSearchRegex(input: String): String {
        val map = mapOf(
            's' to "[sš]",
            'c' to "[cč]",
            'z' to "[zž]",
        )
        val sb = StringBuilder("^") // Anchor at beginning

        for (char in input.lowercase()) {
            sb.append(map[char] ?: Regex.escape(char.toString()))
        }

        return sb.toString()
    }
}