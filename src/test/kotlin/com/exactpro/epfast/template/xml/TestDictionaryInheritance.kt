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
import com.exactpro.epfast.template.xml.TemplateReader.Companion.readTemplatesFromResource
import com.exactpro.epfast.template.xml.TemplateReader.Companion.readTemplatesFromString
import org.junit.jupiter.api.Test

class TestDictionaryInheritance {

    @Test
    fun `ensure dictionary is inherited within int32`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        int32("int") { copy { dictionary = "copy" } }
                    }
                }
        )

        val actual = readTemplatesFromString(
                """
            <templates dictionary="copy" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
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
    fun `ensure dictionary is inherited within uInt32`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        uint32("uInt") { increment { dictionary = "increment" } }
                    }
                }
        )

        val actual = readTemplatesFromString(
                """
            <template name="template" dictionary="increment" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <uInt32 name="uInt">
                    <increment/>
                </uInt32>
            </template>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure dictionary is inherited within int64`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        int64("int") { delta { dictionary = "delta" } }
                    }
                }
        )

        val actual = readTemplatesFromString(
                """
            <templates dictionary="temp" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template" dictionary="delta">
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
    fun `ensure dictionary is inherited within uInt64`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        uint64("uInt") { tail { dictionary = "tail" } }
                    }
                }
        )

        val actual = readTemplatesFromString(
                """
            <templates dictionary="tail" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template">
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
    fun `ensure dictionary is inherited within simpleDecimal`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        simpleDecimal("decimal") { copy { dictionary = "copy" } }
                    }
                }
        )

        val actual = readTemplatesFromString(
                """
            <template name="template" dictionary="copy" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <decimal name="decimal">
                    <copy/>
                </decimal>
            </template>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure dictionary is inherited within compoundDecimal`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        compoundDecimal("decimal") {
                            exponent { delta { dictionary = "delta" } }
                            mantissa { tail { dictionary = "tail" } }
                        }
                    }
                }
        )

        val actual = readTemplatesFromString(
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
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure dictionary is inherited within asciiString`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        asciiString("string") { copy { dictionary = "copy" } }
                    }
                }
        )

        val actual = readTemplatesFromString(
                """
            <template name="template" dictionary="copy" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <string name="string">
                    <copy/>
                </string>
            </template>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure dictionary is inherited within unicodeString`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        unicode("string") { increment { dictionary = "increment" } }
                    }
                }
        )

        val actual = readTemplatesFromString(
                """
            <templates dictionary="increment" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template">
                    <string charset="unicode" name="string">
                        <increment/>
                    </string>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure dictionary is inherited within byteVector`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        byteVector("vector") { delta { dictionary = "delta" } }
                    }
                }
        )
        val actual = readTemplatesFromString(
                """
            <templates dictionary="dictionary" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template" dictionary="delta">
                    <byteVector name="vector">
                        <delta/>
                    </byteVector>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure dictionary is inherited within sequence`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        sequence(null) {
                            length { operator { tail { dictionary = "sequence" } } }
                            instructions {
                                compoundDecimal("decimal") {
                                    mantissa { copy { dictionary = "sequence" } }
                                }
                                sequence(null) {
                                    length { operator { increment { dictionary = "length" } } }
                                }
                            }
                        }
                        sequence(null) {
                            instructions {
                                int32("int") { tail { dictionary = "template" } }
                                sequence(null) {
                                    length { operator { delta { dictionary = "template" } } }
                                    instructions {
                                        sequence(null) {
                                            instructions {
                                                uint64("uInt") { copy { dictionary = "uInt" } }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        )

        val actual = readTemplatesFromResource("dictInheritanceWithSequence.xml")
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure dictionary is inherited within group`() {
        val expected = listOf(
                template("template") {
                    instructions {
                        group(null) {
                            instructions {
                                group(null) {
                                    instructions {
                                        unicode("string") { delta { dictionary = "unicode" } }
                                    }
                                }
                                byteVector("vector") { copy { dictionary = "group" } }
                            }
                        }
                        group(null) {
                            instructions {
                                int64("int") { increment { dictionary = "int" } }
                                group(null) {
                                    instructions {
                                        group(null) {
                                            instructions {
                                                uint32("uInt") { tail { dictionary = "tail" } }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        )
        val actual = readTemplatesFromResource("dictInheritanceWithGroup.xml")

        assertTemplateListsAreEqual(actual, expected)
    }
}
