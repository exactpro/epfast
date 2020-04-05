/******************************************************************************
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
 ******************************************************************************/

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
