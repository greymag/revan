package net.orangedog.revan.modules.trubar.importer

import io.ktor.util.logging.*
import net.orangedog.revan.models.trubar.word.*
import org.xml.sax.Attributes
import org.xml.sax.Locator
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.io.InputStream
import java.text.ParseException
import java.util.*
import javax.xml.parsers.SAXParserFactory

class SloleksXmlParser(private val config: Config = Config.DEFAULT) {
    companion object {
        private val logger = KtorSimpleLogger("revan.trubar.SloleksXmlParser")

        private const val ELEMENT_LEXICON = "lexicon"
        private const val ELEMENT_ENTRY = "entry"

        private const val ELEMENT_HEAD = "head"
        private const val ELEMENT_STATUS = "status"
        private const val ELEMENT_HEADWORD = "headword"
        private const val ELEMENT_LEMMA = "lemma"
        private const val ELEMENT_LEXICAL_UNIT = "lexicalUnit"
        private const val ELEMENT_LEXEME = "lexeme"
        private const val ELEMENT_GRAMMAR = "grammar"
        private const val ELEMENT_CATEGORY = "category"
        private const val ELEMENT_SUBCATEGORY = "subcategory"
        private const val ELEMENT_GRAMMAR_FEATURE = "grammarFeature"
        private const val ELEMENT_MEASURE_LIST = "measureList"
        private const val ELEMENT_MEASURE = "measure"
        private const val ELEMENT_RELATED_ENTRY_LIST = "relatedEntryList"
        private const val ELEMENT_RELATED_ENTRY = "relatedEntry"

        private const val ELEMENT_BODY = "body"
        private const val ELEMENT_WORD_FORM_LIST = "wordFormList"
        private const val ELEMENT_WORD_FORM = "wordForm"
        private const val ELEMENT_MSD = "msd"
        private const val ELEMENT_GRAMMAR_FEATURE_LIST = "grammarFeatureList"
        private const val ELEMENT_FORM_REPRESENTATIONS = "formRepresentations"
        private const val ELEMENT_ORTHOGRAPHY_LIST = "orthographyList"
        private const val ELEMENT_ORTHOGRAPHY = "orthography"
        private const val ELEMENT_FORM = "form"
        private const val ELEMENT_ACCENTUATION_LIST = "accentuationList"
        private const val ELEMENT_ACCENTUATION = "accentuation"
        private const val ELEMENT_PRONUNCIATION_LIST = "pronunciationList"
        private const val ELEMENT_PRONUNCIATION = "pronunciation"
    }

    data class Config (
        val failOnUnexpectedElement: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Config()
        }
    }

    private data class TagContext(
        var handled: Boolean = false,
    )

    private class HandlerImpl(private val config: Config, val onWordCreated: (SloleksWord) -> Unit) : DefaultHandler() {
        private val builder = SloleksWord.Builder()
        private val currentPath = mutableListOf<String>()
        private val attributesStack = Stack<MutableMap<String, Any>>()
        private val textBuffer = StringBuilder()

        private var currentLocator: Locator? = null

        override fun setDocumentLocator(locator: Locator) {
            currentLocator = locator
        }

        override fun startElement(
            uri: String?,
            localName: String?,
            qName: String?,
            attributes: Attributes?
        ) {
            textBuffer.clear()
            if (qName == null) return

            currentPath.add(qName)
            attributesStack.push(mutableMapOf<String, Any>().apply {
                attributes?.let {
                    for (i in 0 until it.length) {
                        this[it.getQName(i)] = it.getValue(i)
                    }
                }
            })
        }

        override fun characters(ch: CharArray, start: Int, length: Int) {
            @Suppress("ReplaceWithStringBuilderAppendRange")
            textBuffer.append(ch, start, length)
        }

        override fun endElement(uri: String?, localName: String?, qName: String?) {
            if (qName == null) return

            val attributes = attributesStack.pop()
            val text = textBuffer.toString().trim()

            try {
                handleElement(qName, attributes, text)
            } catch (e: Exception) {
                logger.error(messageWithPath("Failed with an exception"), e)
                throw e
            } finally {
                currentPath.removeLast()
            }
        }

        private fun getFrequency(attributes: Map<String, Any>, text: String): Int {
            return when (attributes["type"] as? String) {
                "frequency" -> text.toInt()
                else -> throw IllegalArgumentException("Unexpected type: ${attributes["type"]}")
            }
        }

        private fun handleElement(qName: String, attributes: Map<String, Any>, text: String) {
            entry {
                head {
                    headword {
                        when (qName) {
                            ELEMENT_LEMMA -> {
                                builder.setLemma(text)
                            }
                            else -> unexpectedElement(qName)
                        }
                    }

                    lexicalUnit {
                        when (qName) {
                            ELEMENT_LEXEME -> { builder.addLexeme(text) }
                            else -> unexpectedElement(qName)
                        }
                    }

                    grammar {
                        when (qName) {
                            ELEMENT_CATEGORY -> {
                                builder.setCategory(WordCategory.fromString(text))
                            }
                            ELEMENT_SUBCATEGORY -> {
                                builder.setSubcategory(text)
                            }
                            ELEMENT_GRAMMAR_FEATURE -> {
                                when (attributes["name"] as? String) {
                                    "type" -> builder.setType(WordType.fromString(text))
                                    "aspect" -> builder.setAspect(WordAspect.fromString(text))
                                    "gender" -> builder.setGender(WordGender.fromString(text))
                                    "case" -> builder.setCase(WordCase.fromString(text))
                                    "form" -> builder.setForm(WordForm.fromString(text))
                                    else -> unexpectedAttributeValue(qName, attributes, "name")
                                }
                            }
                            else -> unexpectedElement(qName)
                        }
                    }

                    measureList {
                        when (qName) {
                            ELEMENT_MEASURE -> {
                                builder.setFrequency(getFrequency(attributes, text))
                            }
                            else -> unexpectedElement(qName)
                        }
                    }

                    relatedEntryList {
                        when (qName) {
                            ELEMENT_RELATED_ENTRY -> {
                                builder.addRelated(text)
                            }
                            else -> unexpectedElement(qName)
                        }
                    }

                    unhandled {
                        when (qName) {
                            ELEMENT_STATUS -> {
                                builder.setStatus(SloleksEntryStatus.fromString(text))
                            }
                            else -> unexpectedElement(qName)
                        }
                    }
                }

                body {
                    wordFormList {
                        wordForm {
                            val wordFormBuilder = builder.nextWordForm

                            grammarFeatureList {
                                when (qName) {
                                    ELEMENT_GRAMMAR_FEATURE -> {
                                        handleWordFormGrammarFeature(qName, attributes, text)
                                    }
                                    else -> unexpectedElement(qName)
                                }
                            }

                            formRepresentations {
                                orthographyList {
                                    val orthographyBuilder = wordFormBuilder.nextOrthography
                                    orthography {
                                        measureList {
                                            when (qName) {
                                                ELEMENT_MEASURE -> {
                                                    orthographyBuilder.setFrequency(getFrequency(attributes, text))
                                                }
                                                else -> unexpectedElement(qName)
                                            }
                                        }

                                        unhandled {
                                            when (qName) {
                                                ELEMENT_FORM -> {
                                                    orthographyBuilder.setForm(text)
                                                }

                                                else -> unexpectedElement(qName)
                                            }
                                        }
                                    }

                                    when (qName) {
                                        ELEMENT_ORTHOGRAPHY -> {
                                            val norm = attributes["norm"] as? String
                                            if (norm != null) {
                                                orthographyBuilder.setNorm(norm)
                                            }

                                            val morphologyPatterns = attributes["morphologyPatterns"] as? String
                                            if (morphologyPatterns != null) {
                                                orthographyBuilder.setMorphologyPatterns(morphologyPatterns)
                                            }

                                            wordFormBuilder.buildAndAddNextOrthography()
                                        }
                                    }

                                    unhandled { unexpectedElement(qName) }
                                }

                                accentuationList {
                                    accentuation {
                                        when (qName) {
                                            ELEMENT_FORM -> {
                                                wordFormBuilder.addAccentuation(text)
                                            }
                                            else -> unexpectedElement(qName)
                                        }
                                    }
                                }

                                pronunciationList {
                                    pronunciation {
                                        val pronunciationBuilder = wordFormBuilder.nextPronunciation

                                        when (qName) {
                                            ELEMENT_FORM -> {
                                                when (attributes["script"] as? String) {
                                                    "IPA" -> pronunciationBuilder.setIpa(text)
                                                    "SAMPA" -> pronunciationBuilder.setSampa(text)
                                                    else -> unexpectedAttributeValue(qName, attributes, "script")
                                                }
                                            }
                                            else -> unexpectedElement(qName)
                                        }
                                    }

                                    when (qName) {
                                        ELEMENT_PRONUNCIATION -> {
                                            wordFormBuilder.buildAndAddNextPronunciation()
                                        }
                                    }
                                }

                                unhandled { unexpectedElement(qName) }
                            }

                            unhandled {
                                when (qName) {
                                    ELEMENT_MSD -> {
                                        when {
                                            attributes["language"] != "sl" -> unexpectedElement(qName, "Unsupported language: ${attributes["language"]}")
                                            attributes["system"] != "JOS" -> unexpectedElement(qName, "Unsupported system: ${attributes["system"]}")
                                            else -> wordFormBuilder.setJosMs(text)
                                        }
                                    }
                                    else -> unexpectedElement(qName)
                                }
                            }
                        }

                        when (qName) {
                            ELEMENT_WORD_FORM -> {
                                builder.buildAndAddNextWordForm()
                            }
                        }

                        unhandled { unexpectedElement(qName) }
                    }

                    unhandled { unexpectedElement(qName) }
                }

                unhandled { unexpectedElement(qName) }
            }

            when (qName) {
                ELEMENT_ENTRY -> {
                    val word = builder.build()
                    onWordCreated(word)
                    builder.clear()
                }
            }
        }

        private fun handleWordFormGrammarFeature(qName: String, attributes: Map<String, Any>, text: String) {
            val builder = builder.nextWordForm
            when (attributes["name"] as? String) {
                "vform" -> builder.setVForm(WordVForm.fromString(text))
                "number" -> builder.setNumber(WordNumber.fromString(text))
                "owner_number" -> builder.setOwnerNumber(WordNumber.fromString(text))
                "case" -> builder.setCase(WordCase.fromString(text))
                "gender" -> builder.setGender(WordGender.fromString(text))
                "owner_gender" -> builder.setOwnerGender(WordGender.fromString(text))
                "person" -> builder.setPerson(WordPerson.fromString(text))
                "degree" -> builder.setDegree(WordDegree.fromString(text))
                "definiteness" -> builder.setIsDefiniteness(parseBoolean(text))
                "negative" -> builder.setIsNegative(parseBoolean(text))
                "animate" -> builder.setIsAnimate(parseBoolean(text))
                "clitic" -> builder.setClitic(WordClitic.fromString(text))
                else -> unexpectedAttributeValue(qName, attributes, "name")
            }
        }

        override fun endDocument() {
            super.endDocument()
            currentLocator = null
            textBuffer.clear()
            builder.clear()
            currentPath.clear()
            attributesStack.clear()
        }

        private fun entry(func: TagContext.() -> Unit) = TagContext().apply { handle(ELEMENT_ENTRY, func) }
        private fun TagContext.head(func: TagContext.() -> Unit) = handle(ELEMENT_HEAD, func)
        private fun TagContext.headword(func: TagContext.() -> Unit) = handle(ELEMENT_HEADWORD, func)
        private fun TagContext.lexicalUnit(func: TagContext.() -> Unit) = handle(ELEMENT_LEXICAL_UNIT, func)
        private fun TagContext.grammar(func: TagContext.() -> Unit) = handle(ELEMENT_GRAMMAR, func)
        private fun TagContext.measureList(func: TagContext.() -> Unit) = handle(ELEMENT_MEASURE_LIST, func)
        private fun TagContext.relatedEntryList(func: TagContext.() -> Unit) = handle(ELEMENT_RELATED_ENTRY_LIST, func)
        private fun TagContext.body(func: TagContext.() -> Unit) = handle(ELEMENT_BODY, func)
        private fun TagContext.wordFormList(func: TagContext.() -> Unit) = handle(ELEMENT_WORD_FORM_LIST, func)
        private fun TagContext.wordForm(func: TagContext.() -> Unit) = handle(ELEMENT_WORD_FORM, func)
        private fun TagContext.grammarFeatureList(func: TagContext.() -> Unit) = handle(ELEMENT_GRAMMAR_FEATURE_LIST, func)
        private fun TagContext.formRepresentations(func: TagContext.() -> Unit) = handle(ELEMENT_FORM_REPRESENTATIONS, func)
        private fun TagContext.orthographyList(func: TagContext.() -> Unit) = handle(ELEMENT_ORTHOGRAPHY_LIST, func)
        private fun TagContext.orthography(func: TagContext.() -> Unit) = handle(ELEMENT_ORTHOGRAPHY, func)
        private fun TagContext.accentuationList(func: TagContext.() -> Unit) = handle(ELEMENT_ACCENTUATION_LIST, func)
        private fun TagContext.accentuation(func: TagContext.() -> Unit) = handle(ELEMENT_ACCENTUATION, func)
        private fun TagContext.pronunciationList(func: TagContext.() -> Unit) = handle(ELEMENT_PRONUNCIATION_LIST, func)
        private fun TagContext.pronunciation(func: TagContext.() -> Unit) = handle(ELEMENT_PRONUNCIATION, func)
        private fun TagContext.unhandled(func: TagContext.() -> Unit) {
            if (!handled) {
                handled = true
                func(TagContext())
            }
        }

        private fun TagContext.handle(element: String, func: TagContext.() -> Unit) {
            if (currentPath.contains(element)) {
                handled = true
                if (currentPath.last() != element) {
                    func(TagContext())
                }
            }
        }

        private fun unexpectedElement(qName: String, message: String? = null) {
            val msg = messageWithPath(message ?: "Unexpected element <$qName>")
            if (config.failOnUnexpectedElement) {
                throw ParseException(msg, currentLocator?.lineNumber ?: 0)
            } else {
                logger.warn(msg)
            }
        }

        private fun unexpectedAttributeValue(qName: String, attrs: Map<String, Any>, attrName: String) {
            unexpectedElement(qName, "Unexpected attribute value @$attrName: ${attrs[attrName]}")
        }

        private fun messageWithPath(msg: String): String {
            val path = currentPath.joinToString(" -> ")
            return "<$path [${currentLocator?.run { "${lineNumber}:${columnNumber}" } ?: "unknown"}]>: $msg"
        }

        private fun parseBoolean(value: String) = when (value) {
            "yes" -> true
            "no" -> false
            else -> throw IllegalArgumentException("Invalid boolean value: $value")
        }
    }

    fun parse(file: File, handler: (SloleksWord) -> Unit) {
        return parseXmlToWords(file.inputStream(), handler)
    }

    private fun parseXmlToWords(input: InputStream, handler: (SloleksWord) -> Unit) {
        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()

        saxParser.parse(input, HandlerImpl(config, handler))
    }
}
