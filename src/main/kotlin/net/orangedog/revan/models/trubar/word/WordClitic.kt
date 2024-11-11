package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordClitic(val value: String) {
    YES("yes"),
//    NO("no"),
    BOUND("bound");

    companion object {
        fun fromString(value: String): WordClitic = getEnumValue(value) { it.value }
    }
}