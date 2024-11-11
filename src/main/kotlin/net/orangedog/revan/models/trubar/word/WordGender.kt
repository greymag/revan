package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordGender(val value: String) {
    MASCULINE("masculine"),
    FEMININE("feminine"),
    NEUTER("neuter");

    companion object {
        fun fromString(value: String): WordGender = getEnumValue(value) { it.value }
    }
}