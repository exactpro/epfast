package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.LengthField

class LengthFieldBuilder internal constructor(val field: LengthField = LengthField()) {

    var name: String? by javaProperty(field.fieldId::getName, field.fieldId::setName)

    var namespace: String by javaProperty(field.fieldId::getNamespace, field.fieldId::setNamespace)

    var auxiliaryId: String? by javaProperty(field.fieldId::getAuxiliaryId, field.fieldId::setAuxiliaryId)

    fun operator(block: OperatorsBuilder.() -> Unit) {
        field.operator = OperatorsBuilder().apply(block).operator
    }
}
