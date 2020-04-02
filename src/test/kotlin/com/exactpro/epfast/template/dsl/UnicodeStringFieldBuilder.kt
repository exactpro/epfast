package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.UnicodeStringField

class UnicodeStringFieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<UnicodeStringField>(UnicodeStringField(), name, namespace) {

    internal fun build(block: UnicodeStringFieldBuilder.() -> Unit) = apply(block).field
}

internal fun build(name: String, namespace: String, block: UnicodeStringFieldBuilder.() -> Unit): UnicodeStringField =
        UnicodeStringFieldBuilder(name, namespace).build(block)
