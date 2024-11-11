package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordPerson(val value: String) {
    FIRST("first"),
    SECOND("second"),
    THIRD("third");

    companion object {
        fun fromString(value: String): WordPerson = getEnumValue(value) { it.value }
    }
}