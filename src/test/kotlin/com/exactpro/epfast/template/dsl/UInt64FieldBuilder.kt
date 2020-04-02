package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.UInt64Field

class UInt64FieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<UInt64Field>(UInt64Field(), name, namespace) {

    internal fun build(block: UInt64FieldBuilder.() -> Unit) = apply(block).field
}

internal fun build(name: String, namespace: String, block: UInt64FieldBuilder.() -> Unit): UInt64Field =
        UInt64FieldBuilder(name, namespace).build(block)
