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

package com.exactpro.epfast.decoder.message

import com.exactpro.epfast.decoder.IMessage
import com.exactpro.epfast.template.dsl.templates
import com.exactpro.epfast.template.simple.Reference
import com.exactpro.epfast.template.simple.Template
import com.exactpro.epfast.template.simple.Templates
import com.exactpro.junit5.WithByteBuf
import io.netty.buffer.ByteBuf
import java.io.IOException
import org.assertj.core.api.Assertions.assertThat

class TestDecodeMessage {

    private val templates = templates {
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    isOptional = false
                }
                int32("int32_null_1") {
                    isOptional = true
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    isOptional = false
                }
                asciiString("ascii_null_1") {
                    isOptional = true
                }
            }
        }
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    isOptional = false
                }
                int32("int32_null_2") {
                    isOptional = true
                }
                asciiString("ascii_2") {
                    isOptional = false
                }
                templateRef {
                    name = "third template"
                }
                asciiString("ascii_null_2") {
                    isOptional = true
                }
            }
        }
        template("third template") {
            auxiliaryId = "id_3"
            instructions {
                int32("int32_3") {
                    isOptional = false
                }
                int32("int32_null_3") {
                    isOptional = true
                }
                asciiString("ascii_3") {
                    isOptional = false
                }
                asciiString("ascii_null_3") {
                    isOptional = true
                }
            }
        }
    }

    private val templatesWithGroup = templates {
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    isOptional = false
                }
                int32("int32_null_1") {
                    isOptional = true
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    isOptional = false
                }
                asciiString("ascii_null_1") {
                    isOptional = true
                }
            }
        }
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    isOptional = false
                }
                int32("int32_null_2") {
                    isOptional = true
                }
                asciiString("ascii_2") {
                    isOptional = false
                }
                group("group") {
                    instructions {
                        int32("int32_3") {
                            isOptional = false
                        }
                        int32("int32_null_3") {
                            isOptional = true
                        }
                        asciiString("ascii_3") {
                            isOptional = false
                        }
                        asciiString("ascii_null_3") {
                            isOptional = true
                        }
                    }
                }
                asciiString("ascii_null_2") {
                    isOptional = true
                }
            }
        }
    }

    private val templatesWithNestedGroup = templates {
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    isOptional = false
                }
                int32("int32_null_1") {
                    isOptional = true
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    isOptional = false
                }
                asciiString("ascii_null_1") {
                    isOptional = true
                }
            }
        }
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    isOptional = false
                }
                int32("int32_null_2") {
                    isOptional = true
                }
                asciiString("ascii_2") {
                    isOptional = false
                }
                group("group") {
                    instructions {
                        int32("int32_3") {
                            isOptional = false
                        }
                        int32("int32_null_3") {
                            isOptional = true
                        }
                        group("nested group") {
                            instructions {
                                int32("int32_3_nested") {
                                    isOptional = false
                                }
                                int32("int32_null_3_nested") {
                                    isOptional = true
                                }
                                asciiString("ascii_3_nested") {
                                    isOptional = false
                                }
                                asciiString("ascii_null_3_nested") {
                                    isOptional = true
                                }
                            }
                        }
                        asciiString("ascii_3") {
                            isOptional = false
                        }
                        asciiString("ascii_null_3") {
                            isOptional = true
                        }
                    }
                }
                asciiString("ascii_null_2") {
                    isOptional = true
                }
            }
        }
    }

    private val templatesWithOptionalSequence = templates {
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    isOptional = false
                }
                int32("int32_null_1") {
                    isOptional = true
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    isOptional = false
                }
                asciiString("ascii_null_1") {
                    isOptional = true
                }
            }
        }
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    isOptional = false
                }
                int32("int32_null_2") {
                    isOptional = true
                }
                asciiString("ascii_2") {
                    isOptional = false
                }
                sequence("sequence") {
                    isOptional = true
                    length {
                        name = "length"
                    }
                    instructions {
                        int32("int32_3") {
                            isOptional = false
                        }
                        int32("int32_null_3") {
                            isOptional = true
                        }
                        asciiString("ascii_3") {
                            isOptional = false
                        }
                        asciiString("ascii_null_3") {
                            isOptional = true
                        }
                    }
                }
                asciiString("ascii_null_2") {
                    isOptional = true
                }
            }
        }
    }

    private val templatesWithMandatorySequence = templates {
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    isOptional = false
                }
                int32("int32_null_1") {
                    isOptional = true
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    isOptional = false
                }
                asciiString("ascii_null_1") {
                    isOptional = true
                }
            }
        }
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    isOptional = false
                }
                int32("int32_null_2") {
                    isOptional = true
                }
                asciiString("ascii_2") {
                    isOptional = false
                }
                sequence("sequence") {
                    isOptional = false
                    length {
                        name = "length"
                    }
                    instructions {
                        int32("int32_3") {
                            isOptional = false
                        }
                        int32("int32_null_3") {
                            isOptional = true
                        }
                        asciiString("ascii_3") {
                            isOptional = false
                        }
                        asciiString("ascii_null_3") {
                            isOptional = true
                        }
                    }
                }
                asciiString("ascii_null_2") {
                    isOptional = true
                }
            }
        }
    }

    @WithByteBuf(bytesString)
    @Throws(IOException::class)
    fun testTemplateRef(buffers: Collection<ByteBuf>) {
        val templatesMap: Map<Reference, Template> = mapTemplates(templates)

        val handler = ByteBufHandler(templatesMap, Reference("first template", ""))

        var messages: List<Any> = listOf()
        for (buffer in buffers) {
            messages += handler.handle(buffer)
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField("int32_1")).isEqualTo(942755)
        assertThat(message.getField("int32_null_1")).isEqualTo(0)
        assertThat(message.getField("int32_2")).isEqualTo(942755)
        assertThat(message.getField("int32_null_2")).isEqualTo(0)
        assertThat(message.getField("ascii_2")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("int32_3")).isEqualTo(942755)
        assertThat(message.getField("int32_null_3")).isEqualTo(0)
        assertThat(message.getField("ascii_3")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_3")).isEqualTo("ABC")
        assertThat(message.getField("ascii_null_2")).isEqualTo("ABC")
        assertThat(message.getField("ascii_1")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_1")).isEqualTo("ABC")
    }

    @WithByteBuf(bytesString)
    @Throws(IOException::class)
    fun testGroup(buffers: Collection<ByteBuf>) {
        val templatesMap: Map<Reference, Template> = mapTemplates(templatesWithGroup)

        val handler = ByteBufHandler(templatesMap, Reference("first template", ""))

        var messages: List<Any> = listOf()
        for (buffer in buffers) {
            messages += handler.handle(buffer)
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField("int32_1")).isEqualTo(942755)
        assertThat(message.getField("int32_null_1")).isEqualTo(0)
        assertThat(message.getField("int32_2")).isEqualTo(942755)
        assertThat(message.getField("int32_null_2")).isEqualTo(0)
        assertThat(message.getField("ascii_2")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_2")).isEqualTo("ABC")
        assertThat(message.getField("ascii_1")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_1")).isEqualTo("ABC")

        val group: FastMessage = message.getField("group") as FastMessage
        assertThat(group.getField("int32_3")).isEqualTo(942755)
        assertThat(group.getField("int32_null_3")).isEqualTo(0)
        assertThat(group.getField("ascii_3")).isEqualTo("\u0000\u0000")
        assertThat(group.getField("ascii_null_3")).isEqualTo("ABC")
    }

    @WithByteBuf(nestedGroupBytesString)
    @Throws(IOException::class)
    fun testNestedGroup(buffers: Collection<ByteBuf>) {
        val templatesMap: Map<Reference, Template> = mapTemplates(templatesWithNestedGroup)

        val handler = ByteBufHandler(templatesMap, Reference("first template", ""))

        var messages: List<Any> = listOf()
        for (buffer in buffers) {
            messages += handler.handle(buffer)
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField("int32_1")).isEqualTo(942755)
        assertThat(message.getField("int32_null_1")).isEqualTo(0)
        assertThat(message.getField("int32_2")).isEqualTo(942755)
        assertThat(message.getField("int32_null_2")).isEqualTo(0)
        assertThat(message.getField("ascii_2")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_2")).isEqualTo("ABC")
        assertThat(message.getField("ascii_1")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_1")).isEqualTo("ABC")

        val group: FastMessage = message.getField("group") as FastMessage
        assertThat(group.getField("int32_3")).isEqualTo(942755)
        assertThat(group.getField("int32_null_3")).isEqualTo(0)
        assertThat(group.getField("ascii_3")).isEqualTo("\u0000\u0000")
        assertThat(group.getField("ascii_null_3")).isEqualTo("ABC")

        val nestedGroup: FastMessage = group.getField("nested group") as FastMessage
        assertThat(nestedGroup.getField("int32_3_nested")).isEqualTo(942755)
        assertThat(nestedGroup.getField("int32_null_3_nested")).isEqualTo(0)
        assertThat(nestedGroup.getField("ascii_3_nested")).isEqualTo("\u0000\u0000")
        assertThat(nestedGroup.getField("ascii_null_3_nested")).isEqualTo("ABC")
    }

    @WithByteBuf(optionalSequenceBytesString)
    @Throws(IOException::class)
    fun testOptionalSequence(buffers: Collection<ByteBuf>) {
        val templatesMap: Map<Reference, Template> = mapTemplates(templatesWithOptionalSequence)

        val handler = ByteBufHandler(templatesMap, Reference("first template", ""))

        var messages: List<Any> = listOf()
        for (buffer in buffers) {
            messages += handler.handle(buffer)
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField("int32_1")).isEqualTo(942755)
        assertThat(message.getField("int32_null_1")).isEqualTo(0)
        assertThat(message.getField("int32_2")).isEqualTo(942755)
        assertThat(message.getField("int32_null_2")).isEqualTo(0)
        assertThat(message.getField("ascii_2")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_2")).isEqualTo("ABC")
        assertThat(message.getField("ascii_1")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_1")).isEqualTo("ABC")

        val sequence: Array<IMessage> = message.getField("sequence") as Array<IMessage>
        assertThat(sequence.size).isEqualTo(3)

        for (group in sequence) {
            val groupMessage: FastMessage = group as FastMessage
            assertThat(groupMessage.getField("int32_3")).isEqualTo(942755)
            assertThat(groupMessage.getField("int32_null_3")).isEqualTo(0)
            assertThat(groupMessage.getField("ascii_3")).isEqualTo("\u0000\u0000")
            assertThat(groupMessage.getField("ascii_null_3")).isEqualTo("ABC")
        }
    }

    @WithByteBuf(mandatorySequenceBytesString)
    @Throws(IOException::class)
    fun testMandatorySequence(buffers: Collection<ByteBuf>) {
        val templatesMap: Map<Reference, Template> = mapTemplates(templatesWithMandatorySequence)

        val handler = ByteBufHandler(templatesMap, Reference("first template", ""))

        var messages: List<Any> = listOf()
        for (buffer in buffers) {
            messages += handler.handle(buffer)
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField("int32_1")).isEqualTo(942755)
        assertThat(message.getField("int32_null_1")).isEqualTo(0)
        assertThat(message.getField("int32_2")).isEqualTo(942755)
        assertThat(message.getField("int32_null_2")).isEqualTo(0)
        assertThat(message.getField("ascii_2")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_2")).isEqualTo("ABC")
        assertThat(message.getField("ascii_1")).isEqualTo("\u0000\u0000")
        assertThat(message.getField("ascii_null_1")).isEqualTo("ABC")

        val sequence: Array<IMessage> = message.getField("sequence") as Array<IMessage>
        assertThat(sequence.size).isEqualTo(3)

        for (group in sequence) {
            val groupMessage: FastMessage = group as FastMessage
            assertThat(groupMessage.getField("int32_3")).isEqualTo(942755)
            assertThat(groupMessage.getField("int32_null_3")).isEqualTo(0)
            assertThat(groupMessage.getField("ascii_3")).isEqualTo("\u0000\u0000")
            assertThat(groupMessage.getField("ascii_null_3")).isEqualTo("ABC")
        }
    }

    companion object {
        private const val firstT_part1 = "39 45 a3 81 " // 942755, 0
        private const val secondT_part1 = "39 45 a3 81 00 00 80 " // 942755, 0, "\u0000\u0000"
        private const val thirdT = "39 45 a3 81 00 00 80 41 42 c3 " // 942755, 0, "\u0000\u0000", ABC
        private const val secondT_part2 = "41 42 c3 " // ABC
        private const val firstT_part2 = "00 00 80 41 42 c3 " // "\u0000\u0000", ABC
        private const val optionalSequenceLength = "84 "
        private const val mandatorySequenceLength = "83 "

        const val bytesString = firstT_part1 + secondT_part1 + thirdT + secondT_part2 + firstT_part2
        const val optionalSequenceBytesString = firstT_part1 + secondT_part1 + optionalSequenceLength + thirdT + thirdT + thirdT + secondT_part2 + firstT_part2
        const val mandatorySequenceBytesString = firstT_part1 + secondT_part1 + mandatorySequenceLength + thirdT + thirdT + thirdT + secondT_part2 + firstT_part2
        const val nestedGroupBytesString = firstT_part1 + secondT_part1 + firstT_part1 + thirdT + firstT_part2 + secondT_part2 + firstT_part2
    }

    private fun mapTemplates(templates: Templates): Map<Reference, Template> {
        return templates.templates.map {
            Reference(it.templateId.name, it.templateId.namespace) to it
        }.toMap()
    }
}
