package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.ensureNotSet

data class SloleksWordForm(
    // JOS:
    //    ed: Somei, Somer, Somed, Sometn, Somem, Someo
    //    dv: Somdi, Somdr, Somdd, Somdt, Somdm, Somdo
    //    mn: Sommi, Sommr, Sommd, Sommt, Sommm, Sommo
    val josMsd: String,
    val vform: WordVForm?,
    val number: WordNumber?,
    val case: WordCase?,
    val gender: WordGender?,
    val person: WordPerson?,
    val degree: WordDegree?,
    val isDefiniteness: Boolean?,
    val isNegative: Boolean?,
    val isAnimate: Boolean?,
    val clitic: WordClitic?,
    val ownerNumber: WordNumber?,
    val ownerGender: WordGender?,
    val orthographyList: List<SloleksOrthography>,
    val accentuationList: List<String>,
    val pronunciationList: List<SloleksPronunciation>,
) {
    class Builder {
        private var josMs: String? = null
        private var vForm: WordVForm? = null
        private var number: WordNumber? = null
        private var gender: WordGender? = null
        private var person: WordPerson? = null
        private var case: WordCase? = null
        private var degree: WordDegree? = null
        private var isDefiniteness: Boolean? = null
        private var isNegative: Boolean? = null
        private var isAnimate: Boolean? = null
        private var clitic: WordClitic? = null
        private var ownerNumber: WordNumber? = null
        private var ownerGender: WordGender? = null

        private var orthographyList = mutableListOf<SloleksOrthography>()
        private var accentuationList = mutableListOf<String>()
        private var pronunciationList = mutableListOf<SloleksPronunciation>()

        private val nextOrthographyBuilder = SloleksOrthography.Builder()
        private val nextPronunciationBuilder = SloleksPronunciation.Builder()

        val nextOrthography get() = nextOrthographyBuilder
        val nextPronunciation get() = nextPronunciationBuilder

        fun build() = SloleksWordForm(
            josMsd = josMs ?: throw IllegalArgumentException("JOS MSD is required"),
            vform = vForm,
            number = number,
            case = case,
            gender = gender,
            person = person,
            degree = degree,
            isDefiniteness = isDefiniteness,
            isNegative = isNegative,
            isAnimate = isAnimate,
            clitic = clitic,
            ownerNumber = ownerNumber,
            ownerGender = ownerGender,
            orthographyList = orthographyList.toList().ifEmpty { throw IllegalArgumentException("Orthography list is required") },
            accentuationList = accentuationList.toList(),
            pronunciationList = pronunciationList.toList()
        )

        fun setJosMs(value: String) = apply {
            josMs.ensureNotSet("josMs")
            josMs = value
        }

        fun setVForm(value: WordVForm) = apply {
            vForm.ensureNotSet("vForm")
            vForm = value
        }

        fun setNumber(value: WordNumber) = apply {
            number.ensureNotSet("number")
            number = value
        }

        fun setGender(value: WordGender) = apply {
            gender.ensureNotSet("gender")
            gender = value
        }

        fun setPerson(value: WordPerson) = apply {
            person.ensureNotSet("person")
            person = value
        }

        fun setCase(value: WordCase) = apply {
            case.ensureNotSet("case")
            case = value
        }

        fun setDegree(value: WordDegree) = apply {
            degree.ensureNotSet("degree")
            degree = value
        }

        fun setIsDefiniteness(value: Boolean) = apply {
            isDefiniteness.ensureNotSet("isDefiniteness")
            isDefiniteness = value
        }

        fun setIsNegative(value: Boolean) = apply {
            isNegative.ensureNotSet("isNegative")
            isNegative = value
        }

        fun setIsAnimate(value: Boolean) = apply {
            isAnimate.ensureNotSet("isAnimate")
            isAnimate = value
        }

        fun setClitic(value: WordClitic) = apply {
            clitic.ensureNotSet("clitic")
            clitic = value
        }

        fun setOwnerNumber(value: WordNumber) = apply {
            ownerNumber.ensureNotSet("ownerNumber")
            ownerNumber = value
        }

        fun setOwnerGender(value: WordGender) = apply {
            ownerGender.ensureNotSet("ownerGender")
            ownerGender = value
        }

        fun addAccentuation(value: String) = apply {
            accentuationList.add(value)
        }

        fun addOrthography(value: SloleksOrthography) = apply {
            orthographyList.add(value)
        }

        fun buildAndAddNextOrthography() = addOrthography(
            nextOrthographyBuilder.build().also {
                nextOrthographyBuilder.clear()
            }
        )

        fun addPronunciation(value: SloleksPronunciation) = apply {
            pronunciationList.add(value)
        }

        fun buildAndAddNextPronunciation() = addPronunciation(
            nextPronunciationBuilder.build().also {
                nextPronunciationBuilder.clear()
            }
        )

        fun clear() {
            josMs = null
            vForm = null
            number = null
            gender = null
            person = null
            case = null
            degree = null
            isDefiniteness = null
            isNegative = null
            isAnimate = null
            clitic = null
            ownerNumber = null
            ownerGender = null
            orthographyList.clear()
            accentuationList.clear()
            nextOrthographyBuilder.clear()
            nextPronunciationBuilder.clear()
        }
    }
}