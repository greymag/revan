package net.orangedog.revan.repository.trubar

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import net.orangedog.revan.models.trubar.word.SloleksWord
import org.bson.BsonValue
import org.bson.types.ObjectId

interface SloleksWordRepository {
    suspend fun insertOne(word: SloleksWord): BsonValue?
    suspend fun findOneById(objectId: ObjectId): SloleksWord?
}

class SloleksWordRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : SloleksWordRepository {
    companion object {
        private const val COLLECTION = "trubar_sloleks_words"
    }

    override suspend fun insertOne(word: SloleksWord): BsonValue? {
        try {
            val result = mongoDatabase.getCollection<SloleksWord>(COLLECTION).insertOne(word)
            return result.insertedId
        } catch (e: MongoException) {
            // TODO: handle error
            System.err.println("Unable to insert due to an error: $e")
        }
        return null
    }

    override suspend fun findOneById(objectId: ObjectId): SloleksWord? =
        mongoDatabase.getCollection<SloleksWord>(COLLECTION).withDocumentClass<SloleksWord>()
            .find(Filters.eq("_id", objectId))
            .firstOrNull()
}
