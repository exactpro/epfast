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
                    constant {
                        initialValue = "value"
                    }
                    auxiliaryId = "32"
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
                    default {
                        initialValue = "value"
                    }
                    auxiliaryId = "32"
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
                    copy {
                        initialValue = "value"
                        dictionary = "copy"
                    }
                    auxiliaryId = "64"
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
                    increment {
                        dictionary = "template"
                    }
                    auxiliaryId = "64"
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
                    delta {
                        initialValue = "value"
                        dictionary = "delta"
                    }
                    auxiliaryId = "simple"
                    isOptional = true
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
                }
            }
        })

        val actual = WrapperXml.wrap(getResourceInputStream("unicodeString.xml"))
        assertThat(TemplatesComparison.areEqualTemplateLists(actual, expected)).isTrue()
    }

    private fun getResourceInputStream(resourceName: String): InputStream? {
        val thisClass: Class<*> = this.javaClass
        val path = thisClass.getPackage().name.replace(Regex.fromLiteral("."), "/")
        return thisClass.classLoader.getResourceAsStream("$path/$resourceName")
    }
}
