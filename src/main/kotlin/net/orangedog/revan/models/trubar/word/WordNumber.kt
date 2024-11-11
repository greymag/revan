package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordNumber(val value: String) {
    SINGULAR("singular"),
    DUAL("dual"),
    PLURAL("plural");

    companion object {
        fun fromString(value: String): WordNumber = getEnumValue(value) { it.value }
    }
}