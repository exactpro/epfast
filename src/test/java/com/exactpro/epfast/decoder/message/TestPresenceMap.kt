package com.exactpro.epfast.decoder.message

import com.exactpro.epfast.decoder.IMessage
import com.exactpro.epfast.template.dsl.template
import com.exactpro.epfast.template.simple.Reference
import com.exactpro.junit5.WithByteBuf
import io.netty.buffer.ByteBuf
import java.io.IOException
import org.assertj.core.api.Assertions.assertThat

class TestPresenceMap {
    private val templates = listOf(
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    optional = false
                    constant {
                        initialValue = "123"
                    }
                }
                int32("int32_null_1") {
                    optional = true
                    copy {
                        initialValue = "456"
                    }
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    optional = false
                    copy {
                        initialValue = "QWERTY"
                    }
                }
                asciiString("ascii_null_1") {
                    optional = true
                    default {
                        initialValue = "QWERTY"
                    }
                }
            }
        },
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    optional = false
                    delta {
                        initialValue = "0"
                    }
                }
                int32("int32_null_2") {
                    optional = true
                    delta {
                        initialValue = "80"
                    }
                }
                asciiString("ascii_2") {
                    optional = false
                    tail {
                        initialValue = "DEF"
                    }
                }
                group("group") {
                    optional = false
                    instructions {
                        int32("int32_3") {
                            optional = false
                            default {
                                initialValue = "45"
                            }
                        }
                        int32("int32_null_3") {
                            optional = true
                            increment {
                                initialValue = "12"
                            }
                        }
                        asciiString("ascii_3") {
                            optional = false
                            constant {
                                initialValue = "AAA"
                            }
                        }
                        asciiString("ascii_null_3") {
                            optional = true
                            constant {
                                initialValue = "BBB"
                            }
                        }
                    }
                }
                sequence("sequence") {
                    optional = false
                    length {
                        name = "length"
                    }
                    instructions {
                        int32("int32_4") {
                            optional = false
                        }
                        int32("int32_null_4") {
                            optional = true
                        }
                        asciiString("ascii_4") {
                            optional = false
                        }
                        asciiString("ascii_null_4") {
                            optional = true
                        }
                    }
                }
                asciiString("ascii_null_2") {
                    optional = true
                    copy {
                        initialValue = "CCC"
                    }
                }
            }
        }
    )

    @WithByteBuf(ALL_BITS_PRESENT)
    @Throws(IOException::class)
    fun testAllBitsPresent(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templates, Reference("first template", ""))

        val messages: MutableList<Any?> = mutableListOf()
        for (buffer in buffers) {
            messages.addAll(handler.process(buffer))
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField(Reference("int32_1"))).isEqualTo(942755)
        assertThat(message.getField(Reference("int32_null_1"))).isEqualTo(0)
        assertThat(message.getField(Reference("int32_2"))).isEqualTo(942755)
        assertThat(message.getField(Reference("int32_null_2"))).isEqualTo(0)
        assertThat(message.getField(Reference("ascii_2"))).isEqualTo("\u0000\u0000")

        val group: FastMessage = message.getField(Reference("group")) as FastMessage
        assertThat(group.getField(Reference("int32_3"))).isEqualTo(942755)
        assertThat(group.getField(Reference("int32_null_3"))).isEqualTo(0)
        assertThat(group.getField(Reference("ascii_3"))).isEqualTo("\u0000\u0000")
        assertThat(group.getField(Reference("ascii_null_3"))).isEqualTo("ABC")

        val sequence: Array<IMessage> = message.getField(Reference("sequence")) as Array<IMessage>
        assertThat(sequence.size).isEqualTo(3)

        for (sequence_group in sequence) {
            val groupMessage: FastMessage = sequence_group as FastMessage
            assertThat(groupMessage.getField(Reference("int32_4"))).isEqualTo(942755)
            assertThat(groupMessage.getField(Reference("int32_null_4"))).isEqualTo(0)
            assertThat(groupMessage.getField(Reference("ascii_4"))).isEqualTo("\u0000\u0000")
            assertThat(groupMessage.getField(Reference("ascii_null_4"))).isEqualTo("ABC")
        }

        assertThat(message.getField(Reference("ascii_null_2"))).isEqualTo("ABC")
        assertThat(message.getField(Reference("ascii_1"))).isEqualTo("\u0000\u0000")
        assertThat(message.getField(Reference("ascii_null_1"))).isEqualTo("ABC")
    }

    @WithByteBuf(HEX_STRING)
    @Throws(IOException::class)
    fun testRegularPresenceMap(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templates, Reference("first template", ""))

        val messages: MutableList<Any?> = mutableListOf()
        for (buffer in buffers) {
            messages.addAll(handler.process(buffer))
        }

        val message: FastMessage = messages[0] as FastMessage

        assertThat(message.getField(Reference("int32_1"))).isEqualTo(942755)
        assertThat(message.getField(Reference("int32_null_1"))).isEqualTo(-1)
        assertThat(message.getField(Reference("int32_2"))).isEqualTo(942755)
        assertThat(message.getField(Reference("int32_null_2"))).isEqualTo(0)
        assertThat(message.getField(Reference("ascii_2"))).isEqualTo("\u0000\u0000")

        val group: FastMessage = message.getField(Reference("group")) as FastMessage
        assertThat(group.getField(Reference("int32_3"))).isEqualTo(-1)
        assertThat(group.getField(Reference("int32_null_3"))).isEqualTo(-1)
        assertThat(group.getField(Reference("ascii_3"))).isEqualTo("\u0000\u0000")
        assertThat(group.getField(Reference("ascii_null_3"))).isEqualTo("ABC")

        val sequence: Array<IMessage> = message.getField(Reference("sequence")) as Array<IMessage>
        assertThat(sequence.size).isEqualTo(3)

        for (sequence_group in sequence) {
            val groupMessage: FastMessage = sequence_group as FastMessage
            assertThat(groupMessage.getField(Reference("int32_4"))).isEqualTo(942755)
            assertThat(groupMessage.getField(Reference("int32_null_4"))).isEqualTo(0)
            assertThat(groupMessage.getField(Reference("ascii_4"))).isEqualTo("\u0000\u0000")
            assertThat(groupMessage.getField(Reference("ascii_null_4"))).isEqualTo("ABC")
        }

        assertThat(message.getField(Reference("ascii_null_2"))).isEqualTo("ABC")
        assertThat(message.getField(Reference("ascii_1"))).isNull()
        assertThat(message.getField(Reference("ascii_null_1"))).isEqualTo("ABC")
    }

    companion object {
        private const val PM_111 = "87 "
        private const val PM_100 = "84 "
        private const val PM_11 = "83 "
        private const val PM_0 = "80 "

        private const val HEX_STRING = PM_100 + MANDATORY_INT32_942755 +
                PM_11 + MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO +
                PM_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                MANDATORY_UINT32_3 + MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                ASCII_ABC + ASCII_ABC

        private const val ALL_BITS_PRESENT = PM_111 + MANDATORY_INT32_942755 + OPTIONAL_INT32_0 +
                PM_11 + MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO +
                PM_11 + MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                MANDATORY_UINT32_3 + MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                MANDATORY_INT32_942755 + OPTIONAL_INT32_0 + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC +
                ASCII_ABC + MANDATORY_ASCII_ZERO_ZERO + ASCII_ABC
    }
}
