package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.Group

class GroupBuilder internal constructor(name: String, namespace: String) :
    FieldBuilder<Group>(Group(), name, namespace) {

    internal fun build(block: GroupBuilder.() -> Unit) = apply(block).field

    fun typeRef(block: ReferenceBuilder.() -> Unit) {
        field.typeRef = ReferenceBuilder().apply(block).reference
    }

    fun instructions(block: InstructionsBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).apply(block)
    }
}

internal fun build(name: String, namespace: String, block: GroupBuilder.() -> Unit): Group =
        GroupBuilder(name, namespace).build(block)
