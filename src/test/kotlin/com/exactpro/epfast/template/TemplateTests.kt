package com.exactpro.epfast.template

import com.exactpro.epfast.template.assertion.TemplatesComparison
import com.exactpro.epfast.template.dsl.template
import com.exactpro.epfast.template.xml.WrapperXml
import java.io.InputStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TemplateTests {

    @Test
    fun `my first template test`() {
        val expected = listOf(template("template", "tempNS") {
            auxiliaryId = "25"
            instructions {
                int64("") {
                    copy {
                        initialValue = "64"
                        dictionary = "template"
                    }
                }
                int32("") {
                    constant {
                        initialValue = "32"
                    }
                }
                compoundDecimal("") {
                    isOptional = false
                    mantissa {
                        delta { }
                    }
                }
                byteVector("") {
                    isOptional = true
                    length("lengthNAME")
                }
                unicode("") {
                }
            }
        })

        val actual = WrapperXml.wrap(getResourceInputStream("input.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("templateRef.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("int32.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("uInt32.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("int64.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("uInt64.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
    }

    @Test
    fun `test simple decimal`() {
        val expected = listOf(template("template") {
            instructions {
                simpleDecimal("decimal", "namespace") {
                    auxiliaryId = "simple"
                    isOptional = true
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

        val actual = WrapperXml.wrap(getResourceInputStream("simpleDecimal.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("compoundDecimal.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
    }

    @Test
    fun `test ascii string`() {
        val expected = listOf(template("template") {
            instructions {
                asciiString("string", "namespace") {
                    auxiliaryId = "ascii"
                    isOptional = true
                    copy {
                        dictionary = "template"
                        initialValue = "value"
                    }
                }
            }
        })

        val actual = WrapperXml.wrap(getResourceInputStream("asciiString.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("unicodeString.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
    }

    @Test
    fun `test byte vector`() {
        val expected = listOf(template("template") {
            instructions {
                byteVector("vector", "namespace") {
                    auxiliaryId = "byte"
                    isOptional = true
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

        val actual = WrapperXml.wrap(getResourceInputStream("byteVector.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
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

        val actual = WrapperXml.wrap(getResourceInputStream("sequence.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
    }

    @Test
    fun `test group`() {
        val expected = listOf(template("template") {
            instructions {
                group("group") {
                    auxiliaryId = "id"
                    isOptional = true
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

        val actual = WrapperXml.wrap(getResourceInputStream("group.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
    }

    private fun getResourceInputStream(resourceName: String): InputStream? {
        val thisClass: Class<*> = this.javaClass
        val path = thisClass.getPackage().name.replace(Regex.fromLiteral("."), "/")
        return thisClass.classLoader.getResourceAsStream("$path/$resourceName")
    }
}
