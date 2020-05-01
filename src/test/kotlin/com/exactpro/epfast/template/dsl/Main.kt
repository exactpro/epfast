package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.AsciiStringField
import com.exactpro.epfast.template.simple.ByteVectorField
import com.exactpro.epfast.template.simple.CompoundDecimalField
import com.exactpro.epfast.template.simple.DeltaOperator

fun main(args: Array<String>) {
    val template = template("first template") {
        auxiliaryId = "123456789"
        typeRef {
            name = "template"
            namespace = "namespace"
        }
        instructions {
            compoundDecimal("compound") {
                namespace = "decimal"
                exponent {
                    constant {
                        initialValue = "exp test"
                    }
                }
                mantissa {
                    delta {
                        initialValue = "mantissa test"
                        dictionary = "type"
                    }
                }
            }
            asciiString("ascii") {
                auxiliaryId = "123456"
                constant {
                    initialValue = "abc"
                }
            }
            templateRef {
                name = "referenced template name"
                namespace = "namespace"
            }
            byteVector("byte vector") {
                length("name in constructor") {
                    auxiliaryId = "123"
                }
            }
            byteVector("byte vector") {
                length("just name")
            }
            byteVector("byte vector") {
                length {
                    name = "just block"
                    auxiliaryId = "123"
                }
            }
            uint32("uint 32") {
                copy {
                    dictionaryKey = "key as property"
                    dictionaryKey {
                        name = "key as block"
                    }
                }
            }
            asciiString("ascii", "myNamespace") {
                auxiliaryId = "123456"
                constant {
                    initialValue = "abc"
                }
            }
            int32("int 32") {
                increment {
                    dictionary = "type"
                }
            }
            byteVector("byte vector") {
                length {
                    auxiliaryId = "123"
                }
                constant {
                    initialValue = "12451231"
                }
            }
            group("group") {
                instructions {
                    byteVector("byte vector") {
                        length("name")
                        constant {
                            initialValue = "12451231"
                        }
                    }
                }
            }
        }
    }
    println(template.instructions)

    val asciiInstr = template.instructions[1] as AsciiStringField
    val decInstr = template.instructions[0] as CompoundDecimalField
    val byteVectorInstr1 = template.instructions[2] as ByteVectorField
    val byteVectorInstr2 = template.instructions[3] as ByteVectorField
    val byteVectorInstr3 = template.instructions[4] as ByteVectorField
    println(asciiInstr.operator.initialValue)
    println(decInstr.exponent.initialValue)
    println(decInstr.mantissa.initialValue)
    val delta = decInstr.mantissa as DeltaOperator
    println(delta.dictionary.name)
    println(byteVectorInstr1.lengthFieldId.name)
    println(byteVectorInstr1.lengthFieldId.auxiliaryId)
    println(byteVectorInstr2.lengthFieldId.name)
    println(byteVectorInstr2.lengthFieldId.auxiliaryId)
    println(byteVectorInstr3.lengthFieldId.name)
    println(byteVectorInstr3.lengthFieldId.auxiliaryId)
}
