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

class TestTemplateElements {

    @Test
    fun `ensure all FAST template elements can be read (part 1)`() {
        val expected = listOf(
            template("template1", "templateNs1") {
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
                            unicodeString("string", "ns") {
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
                                compoundDecimal("decimal", "ns") { mantissa {} }
                                templateRef {
                                    name = "tempRef"
                                    namespace = "ns"
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
            }
        )
        val actual = readTemplatesFromResource("allElementsPart1.xml")
        assertTemplateListsAreEqual(actual, expected)
    }

    @Test
    fun `ensure all FAST template elements can be read (part 2)`() {
        val expected = listOf(
            template("template1", "templateNs1") {
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
                    unicodeString("unicode", "stringNs") {
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
            }
        )
        val actual = readTemplatesFromResource("allElementsPart2.xml")
        assertTemplateListsAreEqual(actual, expected)
    }
}
