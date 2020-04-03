package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.Instruction
import com.exactpro.epfast.template.Reference.DEFAULT_NAMESPACE

class InstructionsBuilder internal constructor(private val instructions: MutableList<Instruction>) {

    fun asciiString(name: String, namespace: String = DEFAULT_NAMESPACE, block: AsciiStringFieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun unicode(name: String, namespace: String = DEFAULT_NAMESPACE, block: UnicodeStringFieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun byteVector(name: String, namespace: String = DEFAULT_NAMESPACE, block: ByteVectorFieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun int32(name: String, namespace: String = DEFAULT_NAMESPACE, block: Int32FieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun int64(name: String, namespace: String = DEFAULT_NAMESPACE, block: Int64FieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun uint32(name: String, namespace: String = DEFAULT_NAMESPACE, block: UInt32FieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun uint64(name: String, namespace: String = DEFAULT_NAMESPACE, block: UInt64FieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun simpleDecimal(name: String, namespace: String = DEFAULT_NAMESPACE, block: SimpleDecimalFieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun compoundDecimal(name: String, namespace: String = DEFAULT_NAMESPACE, block: CompoundDecimalFieldBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun group(name: String, namespace: String = DEFAULT_NAMESPACE, block: GroupBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }

    fun sequence(name: String, namespace: String = DEFAULT_NAMESPACE, block: SequenceBuilder.() -> Unit) {
        instructions.add(build(name, namespace, block))
    }
}
