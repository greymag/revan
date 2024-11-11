package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordForm(val value: String) {
    LETTER("letter"),
    DIGIT("digit"),
    ROMAN("roman");

    companion object {
        fun fromString(value: String): WordForm = getEnumValue(value) { it.value }
    }
}