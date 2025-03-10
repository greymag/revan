package net.orangedog.revan.models.trubar.api

import kotlinx.serialization.Serializable
import net.orangedog.revan.models.trubar.word.SloleksFindResultEntry

@Serializable
class FindResultDto(
    val list: List<SloleksFindResultEntry>,
)