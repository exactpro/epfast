package com.exactpro.epfast.template

import com.exactpro.epfast.template.assertion.TemplatesComparison
import com.exactpro.epfast.template.dsl.template
import com.exactpro.epfast.template.xml.WrapperXml
import java.io.InputStream
import org.junit.jupiter.api.Test

class TemplateTests {

    @Test
    fun `test template without instructions`() {
        val expected = listOf(template("template", "tempNS") {
            auxiliaryId = "id"
            typeRef {
                name = "typeRef"
                namespace = "namespace"
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("template.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test templateRef`() {
        val expected = listOf(template("template") {
            instructions {
                templateRef {
                    name = "templateRef"
                    namespace = "namespace"
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("templateRef.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test int32`() {
        val expected = listOf(template("template") {
            instructions {
                int32("int", "namespace") {
                    auxiliaryId = "32"
                    constant {
                        initialValue = "value"
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("int32.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test uInt32`() {
        val expected = listOf(template("template") {
            instructions {
                uint32("uInt", "namespace") {
                    auxiliaryId = "32"
                    default {
                        initialValue = "value"
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("uInt32.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test int64`() {
        val expected = listOf(template("template") {
            instructions {
                int64("int", "namespace") {
                    auxiliaryId = "64"
                    copy {
                        initialValue = "value"
                        dictionary = "copy"
                        dictionaryKey {
                            name = "key"
                            namespace = "keyNs"
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("int64.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test uInt64`() {
        val expected = listOf(template("template") {
            instructions {
                uint64("uInt", "namespace") {
                    auxiliaryId = "64"
                    increment {
                        dictionary = "template"
                        dictionaryKey {
                            name = "key"
                            namespace = "keyNs"
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("uInt64.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test simple decimal`() {
        val expected = listOf(template("template") {
            instructions {
                simpleDecimal("decimal", "namespace") {
                    auxiliaryId = "simple"
                    optional = true
                    delta {
                        initialValue = "value"
                        dictionary = "delta"
                        dictionaryKey {
                            name = "key"
                            namespace = "keyNs"
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("simpleDecimal.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test compound decimal`() {
        val expected = listOf(template("template") {
            instructions {
                compoundDecimal("decimal", "namespace") {
                    auxiliaryId = "compound"
                    exponent {
                        tail {
                            dictionary = "type"
                            dictionaryKey {
                                name = "key"
                                namespace = "keyNs"
                            }
                        }
                    }
                    mantissa {
                        tail {
                            dictionary = "mantissa"
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("compoundDecimal.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test ascii string`() {
        val expected = listOf(template("template") {
            instructions {
                asciiString("string", "namespace") {
                    auxiliaryId = "ascii"
                    optional = true
                    copy {
                        dictionary = "template"
                        initialValue = "value"
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("asciiString.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test unicode string`() {
        val expected = listOf(template("template") {
            instructions {
                unicode("string", "namespace") {
                    auxiliaryId = "unicode"
                    increment {
                        dictionary = "type"
                    }
                    length("length") {
                        auxiliaryId = "id"
                        namespace = "namespace"
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("unicodeString.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test byte vector`() {
        val expected = listOf(template("template") {
            instructions {
                byteVector("vector", "namespace") {
                    auxiliaryId = "byte"
                    optional = true
                    constant {
                        initialValue = "value"
                    }
                    length("length") {
                        auxiliaryId = "id"
                        namespace = "namespace"
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("byteVector.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test sequence`() {
        val expected = listOf(template("template") {
            instructions {
                sequence("sequence", "namespace") {
                    auxiliaryId = "id"
                    typeRef {
                        name = "typeRef"
                        namespace = "ns"
                    }
                    length {
                        name = "length"
                        namespace = "ns"
                        auxiliaryId = "id"
                        operator {
                            default {
                                initialValue = "value"
                            }
                        }
                    }
                    instructions {
                        int64("int", "namespace") {
                            copy {
                                dictionaryKey {
                                    name = "key"
                                    namespace = "keyNs"
                                }
                            }
                        }
                        compoundDecimal("decimal", "namespace") {
                            auxiliaryId = "compound"
                            mantissa {
                                delta {
                                    dictionary = "mantissa"
                                }
                            }
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("sequence.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test group`() {
        val expected = listOf(template("template") {
            instructions {
                group("group") {
                    auxiliaryId = "id"
                    optional = true
                    typeRef {
                        name = "typeRef"
                        namespace = "ns"
                    }
                    instructions {
                        byteVector("vector") {
                            auxiliaryId = "byte"
                            constant {
                                initialValue = "value"
                            }
                            length("length") {
                                auxiliaryId = "id"
                            }
                        }
                        asciiString("string") {
                            auxiliaryId = "ascii"
                            copy {
                                dictionary = "template"
                            }
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("group.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    private fun getResourceInputStream(resourceName: String): InputStream? {
        val thisClass: Class<*> = this.javaClass
        val path = thisClass.getPackage().name.replace(Regex.fromLiteral("."), "/")
        return thisClass.classLoader.getResourceAsStream("$path/$resourceName")
    }
}
