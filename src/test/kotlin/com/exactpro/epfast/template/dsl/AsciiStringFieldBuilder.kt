package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.AsciiStringField

class AsciiStringFieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<AsciiStringField>(AsciiStringField(), name, namespace) {

    internal fun build(block: AsciiStringFieldBuilder.() -> Unit) = apply(block).field
}

internal fun build(name: String, namespace: String, block: AsciiStringFieldBuilder.() -> Unit): AsciiStringField =
        AsciiStringFieldBuilder(name, namespace).build(block)
