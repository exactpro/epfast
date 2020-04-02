package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.Dictionary
import com.exactpro.epfast.template.simple.ConstantOperator
import com.exactpro.epfast.template.simple.CopyOperator
import com.exactpro.epfast.template.simple.DefaultOperator
import com.exactpro.epfast.template.simple.DeltaOperator
import com.exactpro.epfast.template.simple.FieldOperator
import com.exactpro.epfast.template.simple.IncrementOperator
import com.exactpro.epfast.template.simple.OperatorWithDictionary
import com.exactpro.epfast.template.simple.Reference
import com.exactpro.epfast.template.simple.TailOperator

abstract class OperatorBuilder<T : FieldOperator>
internal constructor(protected val operator: T) {

    var initialValue: String? by javaProperty(operator::getInitialValue, operator::setInitialValue)
}

abstract class OperatorWithDictionaryBuilder<T : OperatorWithDictionary>
internal constructor(operator: T) : OperatorBuilder<T>(operator) {

    var dictionary: String
        get() = operator.dictionary.name
        set(value) {
            operator.dictionary = Dictionary.getDictionary(value)
        }

    var dictionaryKey: String
        get() = operator.dictionaryKey.name
        set(value) {
            operator.dictionaryKey = Reference(value)
        }

    fun dictionaryKey(block: ReferenceBuilder.() -> Unit) {
        operator.dictionaryKey = ReferenceBuilder().apply(block).reference
    }
}

class ConstantOperatorBuilder internal constructor(operator: ConstantOperator = ConstantOperator()) :
        OperatorBuilder<ConstantOperator>(operator) {

    internal fun build(block: ConstantOperatorBuilder.() -> Unit) = apply(block).operator
}

class DefaultOperatorBuilder internal constructor(operator: DefaultOperator = DefaultOperator()) :
        OperatorBuilder<DefaultOperator>(operator) {

    internal fun build(block: DefaultOperatorBuilder.() -> Unit) = apply(block).operator
}

class CopyOperatorBuilder internal constructor(operator: CopyOperator = CopyOperator()) :
        OperatorWithDictionaryBuilder<CopyOperator>(operator) {

    internal fun build(block: CopyOperatorBuilder.() -> Unit) = apply(block).operator
}

class DeltaOperatorBuilder internal constructor(operator: DeltaOperator = DeltaOperator()) :
        OperatorWithDictionaryBuilder<DeltaOperator>(operator) {

    internal fun build(block: DeltaOperatorBuilder.() -> Unit) = apply(block).operator
}

class IncrementOperatorBuilder internal constructor(operator: IncrementOperator = IncrementOperator()) :
        OperatorWithDictionaryBuilder<IncrementOperator>(operator) {

    internal fun build(block: IncrementOperatorBuilder.() -> Unit) = apply(block).operator
}

class TailOperatorBuilder internal constructor(operator: TailOperator = TailOperator()) :
        OperatorWithDictionaryBuilder<TailOperator>(operator) {

    internal fun build(block: TailOperatorBuilder.() -> Unit) = apply(block).operator
}

internal fun build(block: ConstantOperatorBuilder.() -> Unit): ConstantOperator = ConstantOperatorBuilder().build(block)

internal fun build(block: DefaultOperatorBuilder.() -> Unit): DefaultOperator = DefaultOperatorBuilder().build(block)

internal fun build(block: CopyOperatorBuilder.() -> Unit): CopyOperator = CopyOperatorBuilder().build(block)

internal fun build(block: DeltaOperatorBuilder.() -> Unit): DeltaOperator = DeltaOperatorBuilder().build(block)

internal fun build(block: IncrementOperatorBuilder.() -> Unit): IncrementOperator = IncrementOperatorBuilder().build(block)

internal fun build(block: TailOperatorBuilder.() -> Unit): TailOperator = TailOperatorBuilder().build(block)
