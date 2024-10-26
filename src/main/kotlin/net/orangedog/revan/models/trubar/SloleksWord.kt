package net.orangedog.revan.models.trubar

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SloleksWord(
    @BsonId
    val id: ObjectId,
    val lemma: String,
    val category: String,
    val type: String,
    val gender: String,
    val frequency: Int,
)
