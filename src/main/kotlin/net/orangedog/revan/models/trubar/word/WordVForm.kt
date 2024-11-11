package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordVForm(val value: String) {
    INFINITIVE("infinitive"),
    SUPINE("supine"),
    PARTICIPLE("participle"),
    PRESENT("present"),
    FUTURE("future"),
    IMPERATIVE("imperative"),
    CONDITIONAL("conditional");

    companion object {
        fun fromString(value: String): WordVForm = getEnumValue(value) { it.value }
    }
}