package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.UInt32Field

class UInt32FieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<UInt32Field>(UInt32Field(), name, namespace) {

    internal fun build(block: UInt32FieldBuilder.() -> Unit) = apply(block).field
}

internal fun build(name: String, namespace: String, block: UInt32FieldBuilder.() -> Unit): UInt32Field =
        UInt32FieldBuilder(name, namespace).build(block)
