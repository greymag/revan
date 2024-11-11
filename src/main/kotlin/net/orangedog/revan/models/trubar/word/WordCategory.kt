package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordCategory(val value: String) {
    NOUN("noun"),
    VERB("verb"),
    ADJECTIVE("adjective"),
    ADVERB("adverb"),
    PRONOUN("pronoun"),
    NUMERAL("numeral"),
    PARTICLE("particle"),
    CONJUNCTION("conjunction"),
    INTERJECTION("interjection"),
    PREPOSITION("preposition"),
    ABBREVIATION("abbreviation");

    companion object {
        fun fromString(value: String): WordCategory = getEnumValue(value) { it.value }
    }
}