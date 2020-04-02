package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.Int64Field

class Int64FieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<Int64Field>(Int64Field(), name, namespace) {

    internal fun build(block: Int64FieldBuilder.() -> Unit) = apply(block).field
}

internal fun build(name: String, namespace: String, block: Int64FieldBuilder.() -> Unit): Int64Field =
        Int64FieldBuilder(name, namespace).build(block)
