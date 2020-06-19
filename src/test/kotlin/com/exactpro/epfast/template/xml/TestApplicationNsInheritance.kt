/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.exactpro.epfast.template.xml

import com.exactpro.epfast.template.assertion.TemplatesComparison.assertTemplateListsAreEqual
import com.exactpro.epfast.template.dsl.template
import org.junit.jupiter.api.Test

class TestApplicationNsInheritance {

    @Test
    fun `ensure namespace is inherited within int32`() {
        val expected = listOf(
            template("template", "tempNS") {
                int32("int", "NS") {
                    copy { dictionaryKey { namespace = "NS" } }
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates templateNs="tempNS" ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template">
                    <int32 name="int">
                        <copy/>
                    </int32>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within uInt32`() {
        val expected = listOf(
            template("template", "NS") {
                uint32("uInt", "ns") {
                    increment { dictionaryKey { namespace = "ns" } }
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <template name="template" ns="ns" templateNs="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <uInt32 name="uInt">
                    <increment/>
                </uInt32>
            </template>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within int64`() {
        val expected = listOf(
            template("template") {
                int64("int", "ns") {
                    delta { dictionaryKey { namespace = "ns" } }
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template" ns="ns">
                    <int64 name="int">
                        <delta/>
                    </int64>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within uInt64`() {
        val expected = listOf(
            template("template", "ns") {
                uint64("uInt", "NS") {
                    tail { dictionaryKey { namespace = "NS" } }
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates templateNs="tempNS" ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template" templateNs="ns">
                    <uInt64 name="uInt">
                        <tail/>
                    </uInt64>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within simpleDecimal`() {
        val expected = listOf(
            template("template") {
                simpleDecimal("decimal", "NS") {}
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template">
                    <decimal name="decimal"/>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within compoundDecimal`() {
        val expected = listOf(
            template("template", "NS") {
                compoundDecimal("decimal", "ns") {
                    exponent { tail { dictionaryKey { namespace = "ns" } } }
                    mantissa { delta { dictionaryKey { namespace = "ns" } } }
                }
            }
        )
        val actual = readTemplatesFromString(
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
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within asciiString`() {
        val expected = listOf(
            template("template") {
                asciiString("string", "ns") {}
            }
        )
        val actual = readTemplatesFromString(
            """
            <template name="template" ns="ns" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <string name="string"/>
            </template>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within unicodeString`() {
        val expected = listOf(
            template("template") {
                unicodeString("string", "ns") {
                    length { namespace = "ns" }
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template" ns="ns">
                    <string charset="unicode" name="string"/>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within byteVector`() {
        val expected = listOf(
            template("template") {
                byteVector("vector", "NS") {
                    length("length") { namespace = "NS" }
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template">
                    <byteVector name="vector" lengthName="length"/>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within sequence`() {
        val expected = listOf(
            template("template") {
                sequence("sequence", "ns") {
                    length { namespace = "ns" }
                    instructions {
                        compoundDecimal("decimal", "ns") {
                            exponent { tail { dictionaryKey { namespace = "ns" } } }
                        }
                        sequence("sequence", "sequenceNS") {
                            typeRef { namespace = "sequenceNS" }
                            instructions {
                                asciiString("string", "sequenceNS") {
                                    copy { dictionaryKey { namespace = "sequenceNS" } }
                                }
                            }
                        }
                    }
                }
                sequence("sequence", "namespace") {
                    int32("int", "namespace") {}
                    sequence("sequence", "namespace") {
                        sequence("sequence", "namespace") {
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
        )
        val actual = readTemplatesFromResource("nsInheritanceWithSequence.xml")
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within group`() {
        val expected = listOf(
            template("template") {
                group("group", "ns") {
                    typeRef { namespace = "ns" }
                    instructions {
                        group("group", "groupNS") {
                            unicodeString("string", "groupNS") {
                                length { namespace = "groupNS" }
                            }
                        }
                        byteVector("vector", "ns") {
                            length { namespace = "ns" }
                            tail { dictionaryKey { namespace = "ns" } }
                        }
                    }
                }
                group("group", "namespace") {
                    int64("int", "namespace") {}
                    group("group", "namespace") {
                        typeRef { namespace = "namespace" }
                        instructions {
                            compoundDecimal("decimal", "decimal") {
                                exponent { increment { dictionaryKey { namespace = "decimal" } } }
                                mantissa { copy { dictionaryKey { namespace = "decimal" } } }
                            }
                            group("group", "namespace") {
                                uint32("uInt", "namespace") {
                                    delta { dictionaryKey { namespace = "namespace" } }
                                }
                            }
                        }
                    }
                }
            }
        )
        val actual = readTemplatesFromResource("nsInheritanceWithGroup.xml")
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure namespace is inherited within typeRef`() {
        val expected = listOf(
            template("template1") {
                typeRef { namespace = "NS" }
                instructions {
                    group("group", "NS") { typeRef { namespace = "NS" } }
                }
            },
            template("template2") {
                typeRef { namespace = "ns" }
                instructions {
                    sequence("sequence", "ns") { typeRef { namespace = "ns" } }
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates ns="NS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template1">
                    <typeRef/>
                    <group name="group">
                        <typeRef/>
                    </group>
                </template>
                <template name="template2" ns="ns">
                    <typeRef/>
                    <sequence name="sequence">
                        <typeRef/>
                    </sequence>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }
}
