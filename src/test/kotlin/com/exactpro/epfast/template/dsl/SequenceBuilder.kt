package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.Dictionary
import com.exactpro.epfast.template.simple.Sequence

class SequenceBuilder internal constructor(name: String, namespace: String) :
    FieldBuilder<Sequence>(Sequence(), name, namespace) {

    internal fun build(block: SequenceBuilder.() -> Unit) = apply(block).field

    var dictionary: String
        get() = this.field.dictionary.name
        set(value) {
            this.field.dictionary = Dictionary.getDictionary(value)
        }

    fun typeRef(block: ReferenceBuilder.() -> Unit) {
        field.typeRef = ReferenceBuilder().apply(block).reference
    }

    fun length(block: LengthFieldBuilder.() -> Unit) {
        field.length = LengthFieldBuilder().apply(block).field
    }

    fun instructions(block: InstructionsBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).apply(block)
    }
}

internal fun build(name: String, namespace: String, block: SequenceBuilder.() -> Unit): Sequence =
        SequenceBuilder(name, namespace).build(block)
