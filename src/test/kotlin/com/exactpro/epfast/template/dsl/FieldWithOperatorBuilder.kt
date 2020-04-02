package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.FieldInstructionWithOperator

abstract class FieldWithOperatorBuilder<T : FieldInstructionWithOperator>
internal constructor(field: T, name: String, namespace: String) :
    FieldBuilder<T>(field, name, namespace) {

    fun constant(block: ConstantOperatorBuilder.() -> Unit) {
        field.operator = build(block)
    }

    fun default(block: DefaultOperatorBuilder.() -> Unit) {
        field.operator = build(block)
    }

    fun copy(block: CopyOperatorBuilder.() -> Unit) {
        field.operator = build(block)
    }

    fun delta(block: DeltaOperatorBuilder.() -> Unit) {
        field.operator = build(block)
    }

    fun increment(block: IncrementOperatorBuilder.() -> Unit) {
        field.operator = build(block)
    }

    fun tail(block: TailOperatorBuilder.() -> Unit) {
        field.operator = build(block)
    }
}
