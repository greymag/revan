package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.getEnumValue

enum class SloleksEntryStatus(val value: String) {
    MANUAL("MANUAL"),
    AUTOMATIC("AUTOMATIC");

    companion object {
        fun fromString(value: String): SloleksEntryStatus = getEnumValue(value) { it.value }
    }
}