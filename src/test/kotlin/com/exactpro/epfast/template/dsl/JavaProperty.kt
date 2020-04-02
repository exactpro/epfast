package com.exactpro.epfast.template.dsl

import kotlin.reflect.KProperty

fun <T> javaProperty(getter: () -> T, setter: (T) -> Unit) = JavaProperty(getter, setter)

class JavaProperty<T> internal constructor(private val getter: () -> T, private val setter: (T) -> Unit) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = getter()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = setter(value)
}
