package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordCase(val value: String) {
    NOMINATIVE("nominative"),
    GENITIVE("genitive"),
    DATIVE("dative"),
    ACCUSATIVE("accusative"),
    LOCATIVE("locative"),
    INSTRUMENTAL("instrumental");

    companion object {
        fun fromString(value: String): WordCase = getEnumValue(value) { it.value }
    }
}