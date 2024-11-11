package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.ensureNotSet

data class SloleksOrthography(
    val form: String,
    val frequency: Int?,
    val norm: String?,
    val morphologyPatterns: String?,
) {
    class Builder {
        private var form: String? = null
        private var frequency: Int? = null
        private var norm: String? = null
        private var morphologyPatterns: String? = null

        fun build() = SloleksOrthography(
            form = form ?: throw IllegalArgumentException("Form is required"),
            frequency = frequency,
            norm = norm,
            morphologyPatterns = morphologyPatterns,
        )

        fun setForm(value: String) = apply {
            form.ensureNotSet("form")
            form = value
        }

        fun setFrequency(value: Int) = apply {
            frequency.ensureNotSet("frequency")
            frequency = value
        }

        fun setNorm(value: String) = apply {
            norm.ensureNotSet("norm")
            norm = value
        }

        fun setMorphologyPatterns(value: String) = apply {
            morphologyPatterns.ensureNotSet("morphologyPatterns")
            morphologyPatterns = value
        }

        fun clear() {
            form = null
            frequency = null
            norm = null
            morphologyPatterns = null
        }
    }
}