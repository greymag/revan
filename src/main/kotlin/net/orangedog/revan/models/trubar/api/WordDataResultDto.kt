package net.orangedog.revan.models.trubar.api

import kotlinx.serialization.Serializable
import net.orangedog.revan.models.trubar.word.SloleksWord

@Serializable
data class WordDataResultDto(
    val word: SloleksWord?,
)