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

import com.exactpro.epfast.template.dsl.templates
import com.exactpro.epfast.template.simple.Reference
import com.exactpro.junit5.WithByteBuf
import io.netty.buffer.ByteBuf
import java.io.IOException
import org.assertj.core.api.Assertions

class TestTemplatesWithGroup {
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

    @WithByteBuf(BufferStrings.bytesString)
    @Throws(IOException::class)
    fun testGroup(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templatesWithGroup.templates, Reference("first template", ""))

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

        val group: FastMessage = message.getField(Reference("group")) as FastMessage
        Assertions.assertThat(group.getField(Reference("int32_3"))).isEqualTo(942755)
        Assertions.assertThat(group.getField(Reference("int32_null_3"))).isEqualTo(0)
        Assertions.assertThat(group.getField(Reference("ascii_3"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(group.getField(Reference("ascii_null_3"))).isEqualTo("ABC")
    }

    @WithByteBuf(BufferStrings.nestedGroupBytesString)
    @Throws(IOException::class)
    fun testNestedGroup(buffers: Collection<ByteBuf>) {
        val handler = FastDecoder(templatesWithNestedGroup.templates, Reference("first template", ""))

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

        val group: FastMessage = message.getField(Reference("group")) as FastMessage
        Assertions.assertThat(group.getField(Reference("int32_3"))).isEqualTo(942755)
        Assertions.assertThat(group.getField(Reference("int32_null_3"))).isEqualTo(0)
        Assertions.assertThat(group.getField(Reference("ascii_3"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(group.getField(Reference("ascii_null_3"))).isEqualTo("ABC")

        val nestedGroup: FastMessage = group.getField(Reference("nested group")) as FastMessage
        Assertions.assertThat(nestedGroup.getField(Reference("int32_3_nested"))).isEqualTo(942755)
        Assertions.assertThat(nestedGroup.getField(Reference("int32_null_3_nested"))).isEqualTo(0)
        Assertions.assertThat(nestedGroup.getField(Reference("ascii_3_nested"))).isEqualTo("\u0000\u0000")
        Assertions.assertThat(nestedGroup.getField(Reference("ascii_null_3_nested"))).isEqualTo("ABC")
    }
}
