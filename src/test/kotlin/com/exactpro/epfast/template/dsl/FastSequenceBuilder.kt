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

import com.exactpro.epfast.template.simple.Sequence

class FastSequenceBuilder internal constructor(name: String?, namespace: String) :
    FieldBuilder<Sequence>(Sequence(), name, namespace) {
    internal fun build(block: FastSequenceBuilder.() -> Unit) = apply(block).field

    fun typeRef(block: ReferenceBuilder.() -> Unit) {
        field.typeRef = ReferenceBuilder().apply(block).value
    }

    fun length(block: LengthFieldBuilder.() -> Unit) {
        field.length = LengthFieldBuilder().apply(block).field
    }

    fun instructions(block: InstructionsBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).apply(block)
    }
}

internal fun build(name: String?, namespace: String, block: FastSequenceBuilder.() -> Unit): Sequence =
        FastSequenceBuilder(name, namespace).build(block)
