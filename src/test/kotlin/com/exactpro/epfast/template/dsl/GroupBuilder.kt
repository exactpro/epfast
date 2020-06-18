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

import com.exactpro.epfast.template.Reference
import com.exactpro.epfast.template.simple.Group

class GroupBuilder internal constructor(
    name: String,
    namespace: String
) : FieldBuilder<Group>(Group(), name, namespace) {

    internal fun build(block: GroupBuilder.() -> Unit) = apply(block).field

    fun typeRef(block: ReferenceBuilder.() -> Unit) {
        field.typeRef = ReferenceBuilder().apply(block).value
    }

    fun instructions(block: InstructionsBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).apply(block)
    }

    fun asciiString(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: AsciiStringFieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).asciiString(name, namespace, block)
    }

    fun unicodeString(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: UnicodeStringFieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).unicodeString(name, namespace, block)
    }

    fun byteVector(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: ByteVectorFieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).byteVector(name, namespace, block)
    }

    fun int32(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: Int32FieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).int32(name, namespace, block)
    }

    fun int64(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: Int64FieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).int64(name, namespace, block)
    }

    fun uint32(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: UInt32FieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).uint32(name, namespace, block)
    }

    fun uint64(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: UInt64FieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).uint64(name, namespace, block)
    }

    fun simpleDecimal(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: SimpleDecimalFieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).simpleDecimal(name, namespace, block)
    }

    fun compoundDecimal(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: CompoundDecimalFieldBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).compoundDecimal(name, namespace, block)
    }

    fun group(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: GroupBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).group(name, namespace, block)
    }

    fun sequence(name: String, namespace: String = Reference.DEFAULT_NAMESPACE, block: SequenceBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).sequence(name, namespace, block)
    }

    fun templateRef(block: ReferenceBuilder.() -> Unit) {
        InstructionsBuilder(field.instructions).templateRef(block)
    }
}

internal fun build(name: String, namespace: String, block: GroupBuilder.() -> Unit) =
    GroupBuilder(name, namespace).build(block)
