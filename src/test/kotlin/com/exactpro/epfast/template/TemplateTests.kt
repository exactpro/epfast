package com.exactpro.epfast.template

import com.exactpro.epfast.template.assertion.TemplateAssert.assertThat
import com.exactpro.epfast.template.dsl.template
import com.exactpro.epfast.template.xml.TemplatesXml
import com.exactpro.epfast.template.xml.TemplateXml
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.InputStream
import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller

class TemplateTests {
    private lateinit var templateUnmarshaller: Unmarshaller;

    @BeforeEach
    fun setupUnmarshaller() {
        templateUnmarshaller = JAXBContext.newInstance(TemplatesXml::class.java, TemplateXml::class.java).createUnmarshaller();
    }

    @Test
    fun `my first template test`() {
        val expected = listOf(template("") {
            instructions {
                int64("") {
                    copy {
                        initialValue = "64"
                    }
                }
            }
        })

        val templates: Any = templateUnmarshaller.unmarshal(getResourceInputStream("input.xml"))
        assertThat(templates).isInstanceOf(Templates::class.java)
        val actual = (templates as Templates).templates.first();
        assertThat(actual).isEqualToTemplate(expected.first());
    }

    private fun getResourceInputStream(resourceName: String): InputStream? {
        val thisClass: Class<*> = this.javaClass
        val path = thisClass.getPackage().name.replace(Regex.fromLiteral("."), "/")
        return thisClass.classLoader.getResourceAsStream("$path/$resourceName")
    }

}
