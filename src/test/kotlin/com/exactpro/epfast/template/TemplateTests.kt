package com.exactpro.epfast.template

import com.exactpro.epfast.template.assertion.TemplateAssert.assertThat
import com.exactpro.epfast.template.dsl.template
import com.exactpro.epfast.template.xml.WrapperXml
import java.io.InputStream
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

        val actual = WrapperXml.wrap(getResourceInputStream("input.xml")).first()
        assertThat(actual).isEqualToTemplate(expected.first())
    }

    private fun getResourceInputStream(resourceName: String): InputStream? {
        val thisClass: Class<*> = this.javaClass
        val path = thisClass.getPackage().name.replace(Regex.fromLiteral("."), "/")
        return thisClass.classLoader.getResourceAsStream("$path/$resourceName")
    }
}
