/*
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

abstract class OperatorBuilder<T : FieldOperator> internal constructor(
    protected val operator: T
) {
    var initialValue: String by javaProperty(operator::getInitialValue, operator::setInitialValue)
}

abstract class OperatorWithDictionaryBuilder<T : OperatorWithDictionary> internal constructor(
    operator: T
) : OperatorBuilder<T>(operator) {

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
        operator.dictionaryKey = ReferenceBuilder().apply(block).value
    }
}

class ConstantOperatorBuilder internal constructor(
    operator: ConstantOperator = ConstantOperator()
) : OperatorBuilder<ConstantOperator>(operator) {

    internal fun build(block: ConstantOperatorBuilder.() -> Unit) = apply(block).operator
}

class DefaultOperatorBuilder internal constructor(
    operator: DefaultOperator = DefaultOperator()
) : OperatorBuilder<DefaultOperator>(operator) {

    internal fun build(block: DefaultOperatorBuilder.() -> Unit) = apply(block).operator
}

class CopyOperatorBuilder internal constructor(
    operator: CopyOperator = CopyOperator()
) : OperatorWithDictionaryBuilder<CopyOperator>(operator) {

    internal fun build(block: CopyOperatorBuilder.() -> Unit) = apply(block).operator
}

class DeltaOperatorBuilder internal constructor(
    operator: DeltaOperator = DeltaOperator()
) : OperatorWithDictionaryBuilder<DeltaOperator>(operator) {

    internal fun build(block: DeltaOperatorBuilder.() -> Unit) = apply(block).operator
}

class IncrementOperatorBuilder internal constructor(
    operator: IncrementOperator = IncrementOperator()
) : OperatorWithDictionaryBuilder<IncrementOperator>(operator) {

    internal fun build(block: IncrementOperatorBuilder.() -> Unit) = apply(block).operator
}

class TailOperatorBuilder internal constructor(
    operator: TailOperator = TailOperator()
) : OperatorWithDictionaryBuilder<TailOperator>(operator) {

    internal fun build(block: TailOperatorBuilder.() -> Unit) = apply(block).operator
}

internal fun build(block: ConstantOperatorBuilder.() -> Unit) =
    ConstantOperatorBuilder().build(block)

internal fun build(block: DefaultOperatorBuilder.() -> Unit) =
    DefaultOperatorBuilder().build(block)

internal fun build(block: CopyOperatorBuilder.() -> Unit) =
    CopyOperatorBuilder().build(block)

internal fun build(block: DeltaOperatorBuilder.() -> Unit) =
    DeltaOperatorBuilder().build(block)

internal fun build(block: IncrementOperatorBuilder.() -> Unit) =
    IncrementOperatorBuilder().build(block)

internal fun build(block: TailOperatorBuilder.() -> Unit) =
    TailOperatorBuilder().build(block)
