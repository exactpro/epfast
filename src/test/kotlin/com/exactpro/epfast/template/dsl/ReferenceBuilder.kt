package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.Reference

open class ReferenceBuilder internal constructor(val reference: Reference = Reference()) {

    var name: String? by javaProperty(reference::getName, reference::setName)

    var namespace: String? by javaProperty(reference::getNamespace, reference::setNamespace)
}
