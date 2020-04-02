package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.ByteVectorField
import com.exactpro.epfast.template.simple.Identity

class ByteVectorFieldBuilder internal constructor(name: String, namespace: String) :
    FieldWithOperatorBuilder<ByteVectorField>(ByteVectorField(), name, namespace) {

    internal fun build(block: ByteVectorFieldBuilder.() -> Unit) = apply(block).field

    fun length(name: String) {
        field.lengthFieldId = Identity(name)
    }

    fun length(block: IdentityBuilder.() -> Unit) {
        field.lengthFieldId = IdentityBuilder().apply(block).identity
    }

    fun length(name: String, block: IdentityBuilder.() -> Unit) {
        field.lengthFieldId = IdentityBuilder().apply(block).identity.apply {
            this.name = name
        }
    }
}

internal fun build(name: String, namespace: String, block: ByteVectorFieldBuilder.() -> Unit): ByteVectorField =
        ByteVectorFieldBuilder(name, namespace).build(block)
