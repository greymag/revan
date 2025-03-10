package net.orangedog.revan.models.trubar.word

import kotlinx.serialization.Serializable

@Serializable
data class SloleksFindResultEntry(
    val lemma: String,
    val category: WordCategory,
)