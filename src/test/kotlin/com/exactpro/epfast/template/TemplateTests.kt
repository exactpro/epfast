package com.exactpro.epfast.template

import com.exactpro.epfast.template.assertion.TemplatesComparison
import com.exactpro.epfast.template.dsl.template
import com.exactpro.epfast.template.xml.WrapperXml
import java.io.InputStream
import org.junit.jupiter.api.Test

class TemplateTests {

    @Test
    fun `test all elements part 1`() {
        val expected = listOf(template("template1", "templateNs1") {
            auxiliaryId = "tempId"
            typeRef {
                name = "type"
                namespace = "ref"
            }
            instructions {
                sequence("sequence", "namespace") {
                    auxiliaryId = "id"
                    optional = true
                    typeRef {
                        name = "type"
                        namespace = "ref"
                    }
                    length {
                        name = "length"
                        namespace = "ns"
                        auxiliaryId = "lengthId"
                        operator { constant { initialValue = "value" } }
                    }
                    instructions {
                        int32("int32", "ns") {
                            delta { dictionaryKey { namespace = "ns" } }
                        }
                        unicode("string", "ns") {
                            length {
                                name = "length"
                                namespace = "ns"
                            }
                        }
                    }
                }
                group("group", "namespace") {
                    auxiliaryId = "groupId"
                    optional = false
                    typeRef {
                        name = "type"
                        namespace = "ref"
                    }
                    instructions {
                        sequence("name", "namespace") {
                            typeRef { namespace = "namespace" }
                            instructions {
                                compoundDecimal("decimal", "ns") { mantissa {} }
                                templateRef {
                                    name = "tempRef"
                                    namespace = "ns"
                                }
                            }
                        }
                        byteVector("vector", "ns") {
                            optional = true
                            length {
                                auxiliaryId = "id"
                                namespace = "ns"
                            }
                        }
                    }
                }
            }
        },
                template("template2", "templateNs2") {
                    auxiliaryId = "tempId"
                    typeRef {
                        name = "type"
                        namespace = "ref"
                    }
                    instructions {
                        int32("int", "intNs") {
                            auxiliaryId = "32"
                            optional = false
                            increment {
                                initialValue = "value"
                                dictionary = "template"
                                dictionaryKey {
                                    name = "intKey"
                                    namespace = "ns"
                                }
                            }
                        }
                        int64("int", "intNs") {
                            auxiliaryId = "64"
                            optional = false
                            increment {
                                initialValue = "value"
                                dictionary = "global"
                                dictionaryKey {
                                    name = "intKey"
                                    namespace = "ns"
                                }
                            }
                        }
                        uint32("uInt", "uIntNs") {
                            auxiliaryId = "32"
                            optional = true
                            increment {
                                initialValue = "value"
                                dictionary = "template"
                                dictionaryKey {
                                    name = "intKey"
                                    namespace = "ns"
                                }
                            }
                        }
                        uint64("uInt", "uIntNs") {
                            auxiliaryId = "64"
                            optional = true
                            increment {
                                initialValue = "value"
                                dictionary = "global"
                                dictionaryKey {
                                    name = "intKey"
                                    namespace = "ns"
                                }
                            }
                        }
                    }
                })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("allElementsPart1.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test all elements part 2`() {
        val expected = listOf(template("template1", "templateNs1") {
            auxiliaryId = "tempId"
            typeRef {
                name = "type"
                namespace = "ref"
            }
            instructions {
                asciiString("ascii", "stringNs") {
                    auxiliaryId = "string"
                    optional = true
                    default { initialValue = "value" }
                }
                unicode("unicode", "stringNs") {
                    auxiliaryId = "string"
                    optional = false
                    length("length") {
                        namespace = "stringNs"
                        auxiliaryId = "lengthId"
                    }
                    copy {
                        initialValue = "value"
                        dictionary = "copy"
                        dictionaryKey {
                            name = "key"
                            namespace = "ns"
                        }
                    }
                }
                simpleDecimal("simple", "decimalNs") {
                    auxiliaryId = "decimal"
                    optional = false
                    delta {
                        initialValue = "value"
                        dictionary = "delta"
                        dictionaryKey {
                            name = "key"
                            namespace = "ns"
                        }
                    }
                }
            }
        },
                template("template2", "templateNs2") {
                    auxiliaryId = "tempId"
                    typeRef {
                        name = "type"
                        namespace = "ref"
                    }
                    instructions {
                        compoundDecimal("compound", "decimalNs") {
                            auxiliaryId = "decimal"
                            optional = true
                            mantissa {
                                increment {
                                    initialValue = "value"
                                    dictionary = "increment"
                                    dictionaryKey {
                                        name = "key"
                                        namespace = "ns"
                                    }
                                }
                            }
                            exponent {
                                tail {
                                    initialValue = "value"
                                    dictionary = "tail"
                                    dictionaryKey {
                                        name = "key"
                                        namespace = "ns"
                                    }
                                }
                            }
                        }
                        byteVector("byte", "vectorNs") {
                            auxiliaryId = "vector"
                            optional = true
                            length("length") {
                                namespace = "vectorNs"
                                auxiliaryId = "lengthId"
                            }
                            copy {
                                initialValue = "value"
                                dictionary = "type"
                                dictionaryKey {
                                    name = "key"
                                    namespace = "ns"
                                }
                            }
                        }
                        templateRef {
                            name = "templateRef"
                            namespace = "ns"
                        }
                    }
                })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("allElementsPart2.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance without instructions`() {
        val expected = listOf(template("template1", "tempNS") {
            typeRef {
                name = "typeRef"
                namespace = "NS"
            }
        },
                template("template2", "namespace") {
                    typeRef {
                        name = "typeRef"
                        namespace = "ns"
                    }
                })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates templateNs="tempNS" ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template1" typeRefName="typeRef"/>
                        <template name="template2" ns="ns" templateNs="namespace" typeRefName="typeRef"/>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with templateRef`() {
        val expected = listOf(template("template", "tempNS") {
            instructions {
                templateRef {
                    name = "templateRef"
                    namespace = "tempNS"
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates templateNs="tempNS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template">
                            <templateRef name="templateRef"/>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with int32`() {
        val expected = listOf(template("template", "tempNS") {
            typeRef { namespace = "NS" }
            instructions {
                int32("int", "NS") {
                    copy { dictionaryKey { namespace = "NS" } }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates templateNs="tempNS" ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template">
                            <int32 name="int">
                                <copy/>
                            </int32>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with uInt32`() {
        val expected = listOf(template("template", "NS") {
            typeRef { namespace = "ns" }
            instructions {
                uint32("uInt", "ns") {
                    increment { dictionaryKey { namespace = "ns" } }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" ns="ns" templateNs="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <uInt32 name="uInt">
                            <increment/>
                        </uInt32>
                    </template>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with int64`() {
        val expected = listOf(template("template") {
            typeRef { namespace = "ns" }
            instructions {
                int64("int", "ns") {
                    delta { dictionaryKey { namespace = "ns" } }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template" ns="ns">
                            <int64 name="int">
                                <delta/>
                            </int64>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with uInt64`() {
        val expected = listOf(template("template", "ns") {
            typeRef { namespace = "NS" }
            instructions {
                uint64("uInt", "NS") {
                    tail { dictionaryKey { namespace = "NS" } }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates templateNs="tempNS" ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template" templateNs="ns">
                            <uInt64 name="uInt">
                                <tail/>
                            </uInt64>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with simple decimal`() {
        val expected = listOf(template("template") {
            typeRef { namespace = "NS" }
            instructions {
                simpleDecimal("decimal", "NS") {}
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template">
                            <decimal name="decimal"/>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with compound decimal`() {
        val expected = listOf(template("template", "NS") {
            typeRef { namespace = "ns" }
            instructions {
                compoundDecimal("decimal", "ns") {
                    exponent { tail { dictionaryKey { namespace = "ns" } } }
                    mantissa { delta { dictionaryKey { namespace = "ns" } } }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" ns="ns" templateNs="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <decimal name="decimal">
                            <mantissa>
                                <delta/>
                            </mantissa>
                            <exponent>
                                <tail/>
                            </exponent>
                        </decimal>
                    </template>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with ascii string`() {
        val expected = listOf(template("template") {
            typeRef { namespace = "ns" }
            instructions {
                asciiString("string", "ns") {}
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" ns="ns" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <string name="string"/>
                    </template>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with unicode string`() {
        val expected = listOf(template("template") {
            typeRef { namespace = "ns" }
            instructions {
                unicode("string", "ns") {
                    length("") { namespace = "ns" }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template" ns="ns">
                            <string charset="unicode" name="string"/>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with byte vector`() {
        val expected = listOf(template("template") {
            typeRef { namespace = "NS" }
            instructions {
                byteVector("vector", "NS") {
                    length("length") { namespace = "NS" }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template">
                            <byteVector name="vector" lengthName="length"/>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with sequence`() {
        val expected = listOf(template("template") {
            typeRef { namespace = "ns" }
            instructions {
                sequence("", "ns") {
                    typeRef { namespace = "ns" }
                    length { namespace = "ns" }

                    instructions {
                        compoundDecimal("decimal", "ns") {
                            mantissa { tail { dictionaryKey { namespace = "ns" } } }
                        }
                        sequence("", "sequenceNS") {
                            typeRef { namespace = "sequenceNS" }

                            instructions {
                                asciiString("string", "sequenceNS") {
                                    copy { dictionaryKey { namespace = "sequenceNS" } }
                                }
                            }
                        }
                    }
                }
                sequence("", "namespace") {
                    typeRef { namespace = "namespace" }

                    instructions {
                        int32("int", "namespace") {}
                        sequence("", "namespace") {
                            typeRef { namespace = "namespace" }

                            instructions {
                                sequence("", "namespace") {
                                    typeRef { namespace = "namespace" }
                                    instructions {
                                        uint64("uInt", "namespace") {
                                            increment { dictionaryKey { namespace = "namespace" } }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("nsInheritanceWithSequence.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance with group`() {
        val expected = listOf(template("template") {
            typeRef { namespace = "ns" }
            instructions {
                group("", "ns") {
                    typeRef { namespace = "ns" }

                    instructions {
                        group("", "groupNS") {
                            typeRef { namespace = "groupNS" }

                            instructions {
                                unicode("string", "groupNS") { length { namespace = "groupNS" } }
                            }
                        }
                        byteVector("vector", "ns") {
                            length { namespace = "ns" }
                            tail { dictionaryKey { namespace = "ns" } }
                        }
                    }
                }
                group("", "namespace") {
                    typeRef { namespace = "namespace" }

                    instructions {
                        int64("int", "namespace") {}
                        group("", "namespace") {
                            typeRef { namespace = "namespace" }

                            instructions {
                                compoundDecimal("", "decimal") {
                                    exponent { increment { dictionaryKey { namespace = "decimal" } } }
                                    mantissa { copy { dictionaryKey { namespace = "decimal" } } }
                                }
                                group("", "namespace") {
                                    typeRef { namespace = "namespace" }
                                    instructions {
                                        uint32("uInt", "namespace") {
                                            delta { dictionaryKey { namespace = "namespace" } }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("nsInheritanceWithGroup.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with int32`() {
        val expected = listOf(template("template") {
            instructions {
                int32("int") { copy { dictionary = "copy" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates dictionary="copy" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template">
                            <int32 name="int">
                                <copy/>
                            </int32>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with uInt32`() {
        val expected = listOf(template("template") {
            instructions {
                uint32("uInt") { increment { dictionary = "increment" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" dictionary="increment" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <uInt32 name="uInt">
                            <increment/>
                        </uInt32>
                    </template>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with int64`() {
        val expected = listOf(template("template") {
            instructions {
                int64("int") { delta { dictionary = "delta" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates dictionary="temp" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template" dictionary="delta">
                            <int64 name="int">
                                <delta/>
                            </int64>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with uInt64`() {
        val expected = listOf(template("template") {
            instructions {
                uint64("uInt") { tail { dictionary = "tail" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates dictionary="tail" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template">
                            <uInt64 name="uInt">
                                <tail/>
                            </uInt64>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with simple decimal`() {
        val expected = listOf(template("template") {
            instructions {
                simpleDecimal("decimal") { copy { dictionary = "copy" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" dictionary="copy" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <decimal name="decimal">
                            <copy/>
                        </decimal>
                    </template>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with compound decimal`() {
        val expected = listOf(template("template") {
            instructions {
                compoundDecimal("decimal") {
                    exponent { delta { dictionary = "delta" } }
                    mantissa { tail { dictionary = "tail" } }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" dictionary="delta" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <decimal name="decimal">
                            <mantissa>
                                <tail dictionary="tail"/>
                            </mantissa>
                            <exponent>
                                <delta/>
                            </exponent>
                        </decimal>
                    </template>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with ascii string`() {
        val expected = listOf(template("template") {
            instructions {
                asciiString("string") { copy { dictionary = "copy" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" dictionary="copy" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <string name="string">
                            <copy/>
                        </string>
                    </template>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with unicode string`() {
        val expected = listOf(template("template") {
            instructions {
                unicode("string") { increment { dictionary = "increment" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates dictionary="increment" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template">
                            <string charset="unicode" name="string">
                                <increment/>
                            </string>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with byte vector`() {
        val expected = listOf(template("template") {
            instructions {
                byteVector("vector") { delta { dictionary = "delta" } }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates dictionary="dictionary" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template" dictionary="delta">
                            <byteVector name="vector">
                                <delta/>
                            </byteVector>
                        </template>
                    </templates>
                """.trimIndent().byteInputStream()
        )
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with sequence`() {
        val expected = listOf(template("template") {
            instructions {
                sequence("") {
                    length { operator { tail { dictionary = "sequence" } } }

                    instructions {
                        compoundDecimal("decimal") {
                            mantissa { copy { dictionary = "sequence" } }
                        }
                        sequence("") {
                            length { operator { increment { dictionary = "length" } } }
                        }
                    }
                }
                sequence("") {
                    instructions {
                        int32("int") { tail { dictionary = "template" } }
                        sequence("") {
                            length { operator { delta { dictionary = "template" } } }

                            instructions {
                                sequence("") {

                                    instructions {
                                        uint64("uInt") { copy { dictionary = "uInt" } }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("dictInheritanceWithSequence.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test dictionary inheritance with group`() {
        val expected = listOf(template("template") {
            instructions {
                group("") {

                    instructions {
                        group("") {

                            instructions {
                                unicode("string") { delta { dictionary = "unicode" } }
                            }
                        }
                        byteVector("vector") { copy { dictionary = "group" } }
                    }
                }
                group("") {

                    instructions {
                        int64("int") { increment { dictionary = "int" } }
                        group("") {

                            instructions {
                                group("") {

                                    instructions {
                                        uint32("uInt") { tail { dictionary = "tail" } }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("dictInheritanceWithGroup.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    private fun getResourceInputStream(resourceName: String): InputStream? {
        val thisClass: Class<*> = this.javaClass
        val path = thisClass.getPackage().name.replace(Regex.fromLiteral("."), "/")
        return thisClass.classLoader.getResourceAsStream("$path/$resourceName")
    }
}
