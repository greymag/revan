package net.orangedog.revan.models.trubar.word

import kotlinx.serialization.Serializable
import net.orangedog.revan.models.common.ensureNotSet

@Serializable
data class SloleksPronunciation(
    /**
     * International Phonetic Alphabet.
     */
    val ipa: String,
    /**
     * Speech Assessment Methods Phonetic Alphabet
     */
    val sampa: String,
) {
    class Builder {
        private var ipa: String? = null
        private var sampa: String? = null

        fun build() = SloleksPronunciation(
            ipa = ipa ?: throw IllegalArgumentException("IPA is required"),
            sampa = sampa ?: throw IllegalArgumentException("SAMPA is required"),
        )

        fun setIpa(value: String) = apply {
            ipa.ensureNotSet("ipa")
            ipa = value
        }

        fun setSampa(value: String) = apply {
            sampa.ensureNotSet("sampa")
            sampa = value
        }

        fun clear() {
            ipa = null
            sampa = null
        }
    }
}