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

import com.exactpro.epfast.template.simple.Identity
import com.exactpro.epfast.template.simple.UnicodeStringField

class UnicodeStringFieldBuilder internal constructor(
    name: String,
    namespace: String
) : FieldWithOperatorBuilder<UnicodeStringField>(UnicodeStringField(), name, namespace) {

    internal fun build(block: UnicodeStringFieldBuilder.() -> Unit) = apply(block).field

    fun length(name: String) {
        field.lengthFieldId = Identity(name)
    }

    fun length(block: IdentityBuilder.() -> Unit) {
        field.lengthFieldId = IdentityBuilder().apply(block).value
    }

    fun length(name: String, block: IdentityBuilder.() -> Unit) {
        field.lengthFieldId = IdentityBuilder().also { it.name = name }.apply(block).value
    }
}

internal fun build(name: String, namespace: String, block: UnicodeStringFieldBuilder.() -> Unit) =
    UnicodeStringFieldBuilder(name, namespace).build(block)
