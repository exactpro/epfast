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

import kotlin.reflect.KProperty

fun <T : Any> javaProperty(getter: () -> T?, setter: (T) -> Unit) = JavaProperty(getter, setter)

class JavaProperty<T : Any> internal constructor(
    private val getter: () -> T?,
    private val setter: (T) -> Unit
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getter() ?: throw RuntimeException("Attempt to read uninitialized property " + property.name)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = setter(value)
}
