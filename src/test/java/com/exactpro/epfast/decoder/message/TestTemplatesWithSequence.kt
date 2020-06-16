package com.exactpro.epfast.decoder.message

import com.exactpro.epfast.decoder.IMessage
import com.exactpro.epfast.template.dsl.templates
import com.exactpro.epfast.template.simple.Reference
import com.exactpro.junit5.WithByteBuf
import io.netty.buffer.ByteBuf
import java.io.IOException
import org.assertj.core.api.Assertions

class TestTemplatesWithSequence {

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

    private val templatesWithNullSequence = templates {
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
                }
                asciiString("ascii_null_2") {
                    isOptional = true
                }
            }
        }
    }

    @WithByteBuf(optionalSequenceBytesString)
    @Throws(IOException::class)
    fun testOptionalSequence(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templatesWithOptionalSequence.templates, Reference("first template", ""))

        val messages: MutableList<Any?> = mutableListOf()
        for (buffer in buffers) {
            messages.addAll(handler.process(buffer))
        }

        val message: FastMessage = messages[0] as FastMessage

        Assertions.assertThat(message.getField(Reference("int32_1"))).isEqualTo(942755)
        Assertions.assertThat(message.getField(Reference("int32_null_1"))).isEqualTo(0)
        Assertions.assertThat(message.getField(Reference("int32_2"))).isEqualTo(942755)
        Assertions.assertThat(message.getField(Reference("int32_null_2"))).isEqualTo(0)
        Assertions.assertThat(message.getField(Reference("ascii_2"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(message.getField(Reference("ascii_null_2"))).isEqualTo("ABC")
        Assertions.assertThat(message.getField(Reference("ascii_1"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(message.getField(Reference("ascii_null_1"))).isEqualTo("ABC")

        val sequence: Array<IMessage> = message.getField(Reference("sequence")) as Array<IMessage>
        Assertions.assertThat(sequence.size).isEqualTo(3)

        for (group in sequence) {
            val groupMessage: FastMessage = group as FastMessage
            Assertions.assertThat(groupMessage.getField(Reference("int32_3"))).isEqualTo(942755)
            Assertions.assertThat(groupMessage.getField(Reference("int32_null_3"))).isEqualTo(0)
            Assertions.assertThat(groupMessage.getField(Reference("ascii_3"))).isEqualTo("\u0000\u0000")
            Assertions.assertThat(groupMessage.getField(Reference("ascii_null_3"))).isEqualTo("ABC")
        }
    }

    @WithByteBuf(mandatorySequenceBytesString)
    @Throws(IOException::class)
    fun testMandatorySequence(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templatesWithMandatorySequence.templates, Reference("first template", ""))

        val messages: MutableList<Any?> = mutableListOf()
        for (buffer in buffers) {
            messages.addAll(handler.process(buffer))
        }

        val message: FastMessage = messages[0] as FastMessage

        Assertions.assertThat(message.getField(Reference("int32_1"))).isEqualTo(942755)
        Assertions.assertThat(message.getField(Reference("int32_null_1"))).isEqualTo(0)
        Assertions.assertThat(message.getField(Reference("int32_2"))).isEqualTo(942755)
        Assertions.assertThat(message.getField(Reference("int32_null_2"))).isEqualTo(0)
        Assertions.assertThat(message.getField(Reference("ascii_2"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(message.getField(Reference("ascii_null_2"))).isEqualTo("ABC")
        Assertions.assertThat(message.getField(Reference("ascii_1"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(message.getField(Reference("ascii_null_1"))).isEqualTo("ABC")

        val sequence: Array<IMessage> = message.getField(Reference("sequence")) as Array<IMessage>
        Assertions.assertThat(sequence.size).isEqualTo(3)

        for (group in sequence) {
            val groupMessage: FastMessage = group as FastMessage
            Assertions.assertThat(groupMessage.getField(Reference("int32_3"))).isEqualTo(942755)
            Assertions.assertThat(groupMessage.getField(Reference("int32_null_3"))).isEqualTo(0)
            Assertions.assertThat(groupMessage.getField(Reference("ascii_3"))).isEqualTo("\u0000\u0000")
            Assertions.assertThat(groupMessage.getField(Reference("ascii_null_3"))).isEqualTo("ABC")
        }
    }

    @WithByteBuf(nullSequenceBytesString)
    @Throws(IOException::class)
    fun testNullSequence(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templatesWithNullSequence.templates, Reference("first template", ""))

        val messages: MutableList<Any?> = mutableListOf()
        for (buffer in buffers) {
            messages.addAll(handler.process(buffer))
        }

        val message: FastMessage = messages[0] as FastMessage

        Assertions.assertThat(message.getField(Reference("int32_1"))).isEqualTo(942755)
        Assertions.assertThat(message.getField(Reference("int32_null_1"))).isEqualTo(0)
        Assertions.assertThat(message.getField(Reference("int32_2"))).isEqualTo(942755)
        Assertions.assertThat(message.getField(Reference("int32_null_2"))).isEqualTo(0)
        Assertions.assertThat(message.getField(Reference("ascii_2"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(message.getField(Reference("ascii_null_2"))).isEqualTo("ABC")
        Assertions.assertThat(message.getField(Reference("ascii_1"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(message.getField(Reference("ascii_null_1"))).isEqualTo("ABC")
    }

    companion object {

        const val optionalSequenceBytesString = FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 +
                FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 + FastSnippets.ASCII_ZERO +
                FastSnippets.OPTIONAL_UINT32_3 + FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 +
                FastSnippets.ASCII_ZERO + FastSnippets.ABC + FastSnippets.MANDATORY_INT32_942755 +
                FastSnippets.OPTIONAL_INT32_0 + FastSnippets.ASCII_ZERO + FastSnippets.ABC +
                FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 + FastSnippets.ASCII_ZERO +
                FastSnippets.ABC + FastSnippets.ABC + FastSnippets.ASCII_ZERO + FastSnippets.ABC

        const val mandatorySequenceBytesString = FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 +
                FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 + FastSnippets.ASCII_ZERO +
                FastSnippets.MANDATORY_UINT32_3 + FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 +
                FastSnippets.ASCII_ZERO + FastSnippets.ABC + FastSnippets.MANDATORY_INT32_942755 +
                FastSnippets.OPTIONAL_INT32_0 + FastSnippets.ASCII_ZERO + FastSnippets.ABC +
                FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 + FastSnippets.ASCII_ZERO +
                FastSnippets.ABC + FastSnippets.ABC + FastSnippets.ASCII_ZERO + FastSnippets.ABC

        const val nullSequenceBytesString = FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 +
                FastSnippets.MANDATORY_INT32_942755 + FastSnippets.OPTIONAL_INT32_0 + FastSnippets.ASCII_ZERO +
                FastSnippets.OPTIONAL_UINT32_NULL + FastSnippets.ABC + FastSnippets.ASCII_ZERO + FastSnippets.ABC
    }
}
