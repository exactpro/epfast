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

import com.exactpro.epfast.template.Reference.DEFAULT_NAMESPACE
import com.exactpro.epfast.template.simple.Sequence

class SequenceBuilder internal constructor(
    name: String,
    namespace: String
) : FieldBuilder<Sequence>(Sequence(), name, namespace) {

    internal fun build(block: SequenceBuilder.() -> Unit) = apply(block).field

    fun typeRef(block: ReferenceBuilder.() -> Unit) {
        field.typeRef = ReferenceBuilder().apply(block).value
    }

    fun length(block: LengthFieldBuilder.() -> Unit) {
        field.length = LengthFieldBuilder().apply(block).field
    }

    fun length(name: String, block: LengthFieldBuilder.() -> Unit) {
        field.length = LengthFieldBuilder().also { it.name = name }.apply(block).field
    }

    fun instructions(block: InstructionsBuilder.() -> Unit) =
        InstructionsBuilder(field.instructions).apply(block)

    fun asciiString(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: AsciiStringFieldBuilder.() -> Unit = {}
    ) =
        instructions { asciiString(name, namespace, block) }

    fun unicodeString(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: UnicodeStringFieldBuilder.() -> Unit = {}
    ) =
        instructions { unicodeString(name, namespace, block) }

    fun byteVector(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: ByteVectorFieldBuilder.() -> Unit = {}
    ) =
        instructions { byteVector(name, namespace, block) }

    fun int32(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: Int32FieldBuilder.() -> Unit = {}
    ) =
        instructions { int32(name, namespace, block) }

    fun int64(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: Int64FieldBuilder.() -> Unit = {}
    ) =
        instructions { int64(name, namespace, block) }

    fun uint32(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: UInt32FieldBuilder.() -> Unit = {}
    ) =
        instructions { uint32(name, namespace, block) }

    fun uint64(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: UInt64FieldBuilder.() -> Unit = {}
    ) =
        instructions { uint64(name, namespace, block) }

    fun simpleDecimal(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: SimpleDecimalFieldBuilder.() -> Unit = {}
    ) =
        instructions { simpleDecimal(name, namespace, block) }

    fun compoundDecimal(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: CompoundDecimalFieldBuilder.() -> Unit
    ) =
        instructions { compoundDecimal(name, namespace, block) }

    fun group(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: GroupBuilder.() -> Unit
    ) =
        instructions { group(name, namespace, block) }

    fun sequence(
        name: String,
        namespace: String = DEFAULT_NAMESPACE,
        block: SequenceBuilder.() -> Unit
    ) =
        instructions { sequence(name, namespace, block) }

    fun templateRef(block: ReferenceBuilder.() -> Unit) =
        instructions { templateRef(block) }
}

internal fun build(name: String, namespace: String, block: SequenceBuilder.() -> Unit) =
    SequenceBuilder(name, namespace).build(block)
