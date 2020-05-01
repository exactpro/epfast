package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.Dictionary
import com.exactpro.epfast.template.Reference.DEFAULT_NAMESPACE
import com.exactpro.epfast.template.simple.Template

class TemplateBuilder internal constructor(val template: Template) {

    internal constructor(name: String, namespace: String, template: Template) : this(template) {
        template.templateId.name = name
        template.templateId.namespace = namespace
    }

    var name: String? by javaProperty(template.templateId::getName, template.templateId::setName)

    var namespace: String by javaProperty(template.templateId::getNamespace, template.templateId::setNamespace)

    var auxiliaryId: String? by javaProperty(template.templateId::getAuxiliaryId, template.templateId::setAuxiliaryId)

    var dictionary: String
        get() = template.dictionary.name
        set(value) {
            template.dictionary = Dictionary.getDictionary(value)
        }

    fun typeRef(block: ReferenceBuilder.() -> Unit) {
        template.typeRef = ReferenceBuilder().apply(block).reference
    }

    fun instructions(block: InstructionsBuilder.() -> Unit) {
        InstructionsBuilder(template.instructions).apply(block)
    }
}

fun template(name: String, namespace: String = DEFAULT_NAMESPACE, block: TemplateBuilder.() -> Unit): Template =
        TemplateBuilder(name, namespace, Template()).apply(block).template
