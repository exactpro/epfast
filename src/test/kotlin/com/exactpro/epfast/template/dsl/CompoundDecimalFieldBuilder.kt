package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.CompoundDecimalField

class CompoundDecimalFieldBuilder internal constructor(name: String, namespace: String) :
    FieldBuilder<CompoundDecimalField>(CompoundDecimalField(), name, namespace) {

    internal fun build(block: CompoundDecimalFieldBuilder.() -> Unit) = apply(block).field

    fun exponent(block: OperatorsBuilder.() -> Unit) {
        field.exponent = OperatorsBuilder().apply(block).operator
    }

    fun mantissa(block: OperatorsBuilder.() -> Unit) {
        field.mantissa = OperatorsBuilder().apply(block).operator
    }
}

internal fun build(name: String, namespace: String, block: CompoundDecimalFieldBuilder.() -> Unit): CompoundDecimalField =
        CompoundDecimalFieldBuilder(name, namespace).build(block)
