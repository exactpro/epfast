package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.Int32Field

class Int32FieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<Int32Field>(Int32Field(), name, namespace) {

    internal fun build(block: Int32FieldBuilder.() -> Unit) = apply(block).field
}

internal fun build(name: String, namespace: String, block: Int32FieldBuilder.() -> Unit): Int32Field =
        Int32FieldBuilder(name, namespace).build(block)
