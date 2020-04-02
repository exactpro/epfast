package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.SimpleDecimalField

class SimpleDecimalFieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<SimpleDecimalField>(SimpleDecimalField(), name, namespace) {

    internal fun build(block: SimpleDecimalFieldBuilder.() -> Unit) = apply(block).field
}

internal fun build(name: String, namespace: String, block: SimpleDecimalFieldBuilder.() -> Unit): SimpleDecimalField =
        SimpleDecimalFieldBuilder(name, namespace).build(block)
