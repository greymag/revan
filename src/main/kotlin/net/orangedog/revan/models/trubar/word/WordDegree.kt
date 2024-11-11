package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordDegree(val value: String) {
    POSITIVE("positive"),
    SUPERLATIVE("superlative"),
    COMPARATIVE("comparative");

    companion object {
        fun fromString(value: String): WordDegree = getEnumValue(value) { it.value }
    }
}