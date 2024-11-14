package net.orangedog.revan.models.trubar.word

import net.orangedog.revan.models.common.ensureNotSet
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SloleksWord(
//    @BsonId
//    val id: ObjectId,
    var status: SloleksEntryStatus,
    val lemma: String,
    val lexeme: List<String>,
    val category: WordCategory,
    val subcategory: String? = null,
    val type: WordType?,
    val aspect: WordAspect?,
    val gender: WordGender?,
    val case: WordCase?,
    val form: WordForm?,

    val frequency: Int,

    val related: List<String>,
    val wordForms: List<SloleksWordForm>,
) {
    class Builder {
//        private var id: ObjectId? = null
        private var status: SloleksEntryStatus? = null
        private var lemma: String? = null
        private var lexeme = mutableListOf<String>()
        private var category: WordCategory? = null
        private var subcategory: String? = null
        private var type: WordType? = null
        private var aspect: WordAspect? = null
        private var gender: WordGender? = null
        private var case: WordCase? = null
        private var form: WordForm? = null
        private var frequency: Int? = null
        private var related = mutableListOf<String>()
        private var wordForms = mutableListOf<SloleksWordForm>()

        private val nextWordFormBuilder = SloleksWordForm.Builder()

        val nextWordForm get() = nextWordFormBuilder

        fun build(): SloleksWord {
            return SloleksWord(
//                id = id ?: ObjectId(),
                status = status ?: throw IllegalArgumentException("Status is required"),
                lemma = lemma ?: throw IllegalArgumentException("Lemma is required"),
                lexeme = lexeme.toList(),
                category = category ?: throw IllegalArgumentException("Category is required"),
                subcategory = subcategory,
                type = type,
                aspect = aspect,
                gender = gender,
                case = case,
                form = form,
                frequency = frequency ?: throw IllegalArgumentException("Frequency is required"),
                related = related.toList(),
                wordForms = wordForms.toList()
            )
        }

        fun setLemma(value: String) = apply {
            lemma.ensureNotSet("lemma")
            lemma = value
        }

        fun setStatus(value: SloleksEntryStatus) = apply {
            status.ensureNotSet("status")
            status = value
        }

        fun addLexeme(value: String) = apply { lexeme.add(value) }

        fun setCategory(value: WordCategory) = apply {
            category.ensureNotSet("category")
            category = value
        }

        fun setSubcategory(value: String) = apply {
            subcategory.ensureNotSet("subcategory")
            subcategory = value
        }

        fun setType(value: WordType) = apply {
            type.ensureNotSet("type")
            type = value
        }

        fun setAspect(value: WordAspect) = apply {
            aspect.ensureNotSet("aspect")
            aspect = value
        }

        fun setFrequency(value: Int) = apply {
            frequency.ensureNotSet("frequency")
            frequency = value
        }

        fun setGender(value: WordGender) = apply {
            gender.ensureNotSet("gender")
            gender = value
        }

        fun setCase(value: WordCase) = apply {
            case.ensureNotSet("case")
            case = value
        }

        fun setForm(value: WordForm) = apply {
            form.ensureNotSet("form")
            form = value
        }

        fun addRelated(value: String) = apply {
            related.add(value)
        }

        fun addWordForm(value: SloleksWordForm) = apply {
            wordForms.add(value)
        }

        fun buildAndAddNextWordForm() = addWordForm(
            nextWordFormBuilder.build().also {
                nextWordFormBuilder.clear()
            }
        )

        fun clear() {
//            id = null
            status = null
            lemma = null
            lexeme.clear()
            category = null
            subcategory = null
            type = null
            aspect = null
            gender = null
            case = null
            form = null
            frequency = null
            related.clear()
            wordForms.clear()

            nextWordFormBuilder.clear()
        }
    }
}