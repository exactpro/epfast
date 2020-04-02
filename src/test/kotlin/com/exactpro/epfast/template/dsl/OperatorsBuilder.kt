package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.FieldOperator

class OperatorsBuilder {

    var operator: FieldOperator? = null

    fun constant(block: ConstantOperatorBuilder.() -> Unit) {
        operator = build(block)
    }

    fun default(block: DefaultOperatorBuilder.() -> Unit) {
        operator = build(block)
    }

    fun copy(block: CopyOperatorBuilder.() -> Unit) {
        operator = build(block)
    }

    fun delta(block: DeltaOperatorBuilder.() -> Unit) {
        operator = build(block)
    }

    fun increment(block: IncrementOperatorBuilder.() -> Unit) {
        operator = build(block)
    }

    fun tail(block: TailOperatorBuilder.() -> Unit) {
        operator = build(block)
    }
}
