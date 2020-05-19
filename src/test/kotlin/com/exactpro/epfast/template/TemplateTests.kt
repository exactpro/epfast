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
                        int32("int32", "ns") { delta {} }
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
                            typeRef {
                                namespace = "namespace"
                            }
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
                namespace = "ns"
            }
        },
                template("template2", "tempNS") {
                    typeRef {
                        name = "typeRef"
                        namespace = "namespace"
                    }
                })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <templates templateNs="tempNS" ns="ns" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <template name="template1" typeRefName="typeRef"/>
                        <template name="template2" ns="namespace" typeRefName="typeRef"/>
                    </templates>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <templateRef name="templateRef" templateNs="namespace"/>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <int32 name="int" id="32" ns="namespace">
                            <constant value="value"/>
                        </int32>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <uInt32 name="uInt" id="32" ns="namespace">
                            <default value="value"/>
                        </uInt32>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <int64 name="int" id="64" ns="namespace">
                            <copy value="value" dictionary="copy" keyName="key" keyNs="keyNs"/>
                        </int64>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <uInt64 name="uInt" id="64" ns="namespace">
                            <increment dictionary="template" keyName="key" keyNs="keyNs"/>
                        </uInt64>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <decimal name="decimal" id="simple" ns="namespace" presence="optional">
                            <delta value="value" dictionary="delta" keyName="key" keyNs="keyNs"/>
                        </decimal>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <string name="string" id="ascii" ns="namespace" presence="optional">
                            <copy dictionary="template" value="value"/>
                        </string>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <string charset="unicode" name="string" id="unicode" ns="namespace" lengthName="length" lengthId="id">
                            <increment dictionary="type"/>
                        </string>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

        val actual = WrapperXml.wrapXmlInFASTTemplateList(
                """
                    <template name="template" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                        <byteVector name="vector" id="byte" ns="namespace" presence="optional" lengthName="length" lengthId="id">
                            <constant value="value"/>
                        </byteVector>
                    </template>
                """.trimIndent().byteInputStream()
        )
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

    @Test
    fun `test dictionary inheritance`() {
        val expected = listOf(template("template") {
            instructions {
                group("group") {
                    instructions {
                        sequence("sequence") {
                            length {
                                name = "length"
                                operator {
                                    tail {
                                        dictionary = "tail"
                                    }
                                }
                            }
                        }
                        unicode("string") {
                            copy {
                                dictionary = "copy"
                            }
                        }
                    }
                }
                int32("int32") {
                    increment {
                        dictionary = "increment"
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("dictionary.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `test namespace inheritance`() {
        val expected = listOf(template("template", "template") {
            typeRef {
                namespace = "group"
            }
            instructions {
                group("group", "group") {
                    typeRef {
                        namespace = "group"
                    }
                    instructions {
                        sequence("sequence", "copy") {
                            typeRef {
                                namespace = "copy"
                            }
                            length {
                                name = "length"
                                namespace = "tail"
                                operator {
                                    tail {
                                        namespace = "tail"
                                    }
                                }
                            }
                            instructions {
                                asciiString("string", "copy") {
                                    copy {
                                        namespace = "copy"
                                    }
                                }
                            }
                        }
                    }
                }
                int32("int32", "increment") {
                    increment {
                        namespace = "increment"
                    }
                }
            }
        })

        val actual = WrapperXml.wrapXmlInFASTTemplateList(getResourceInputStream("namespace.xml"))
        TemplatesComparison.assertTemplateListsAreEqual(actual, expected)
    }

    private fun getResourceInputStream(resourceName: String): InputStream? {
        val thisClass: Class<*> = this.javaClass
        val path = thisClass.getPackage().name.replace(Regex.fromLiteral("."), "/")
        return thisClass.classLoader.getResourceAsStream("$path/$resourceName")
    }
}
