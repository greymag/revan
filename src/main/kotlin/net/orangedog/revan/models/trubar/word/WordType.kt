package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordType(val value: String) {
    MAIN("main"),
    GENERAL("general"),
    COMMON("common"),
    PARTICIPLE("participle"),
    AUXILIARY("auxiliary"),
    SUBORDINATING("subordinating"),
    COORDINATING("coordinating"),
    POSSESSIVE("possessive"),
    PROPER("proper"),
    INDEFINITE("indefinite"),
    SPECIAL("special"),
    ORDINAL("ordinal"),
    CARDINAL("cardinal"),
    REFLEXIVE("reflexive"),
    PERSONAL("personal"),
    DEMONSTRATIVE("demonstrative"),
    PRONOMINAL("pronominal"),
    INTERROGATIVE("interrogative"),
    RELATIVE("relative"),
    NEGATIVE("negative");

    companion object {
        fun fromString(value: String): WordType = getEnumValue(value) { it.value }
    }
}