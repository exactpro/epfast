package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.TemplateRef

class TemplateRefBuilder internal constructor(private val instruction: TemplateRef = TemplateRef()) {
    fun templateRef(block: ReferenceBuilder.() -> Unit) {
        instruction.templateRef = ReferenceBuilder().apply(block).reference
    }
}
