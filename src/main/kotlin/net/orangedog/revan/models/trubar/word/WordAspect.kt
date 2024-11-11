package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class WordAspect(val value: String) {
    PROGRESSIVE("progressive"),
    PERFECTIVE("perfective"),
    BIASPECTUAL("biaspectual");

    companion object {
        fun fromString(value: String): WordAspect = getEnumValue(value) { it.value }
    }
}