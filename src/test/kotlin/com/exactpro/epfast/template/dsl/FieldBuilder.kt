package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.FieldInstruction

abstract class FieldBuilder<T : FieldInstruction>
internal constructor(protected val field: T, name: String, namespace: String) {

    init {
        field.fieldId.name = name
        field.fieldId.namespace = namespace
    }

    var name: String? by javaProperty(field.fieldId::getName, field.fieldId::setName)

    var namespace: String by javaProperty(field.fieldId::getNamespace, field.fieldId::setNamespace)

    var auxiliaryId: String? by javaProperty(field.fieldId::getAuxiliaryId, field.fieldId::setAuxiliaryId)

    var isOptional: Boolean by javaProperty(field::isOptional, field::setOptional)
}
