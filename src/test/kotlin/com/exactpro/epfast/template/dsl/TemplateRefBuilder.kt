package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.TemplateRef

class TemplateRefBuilder internal constructor(private val instruction: TemplateRef = TemplateRef()) {
    internal fun build(block: ReferenceBuilder.() -> Unit): TemplateRef {
        instruction.templateRef = ReferenceBuilder().apply(block).reference
        return instruction
    }
}

internal fun build(block: ReferenceBuilder.() -> Unit): TemplateRef =
        TemplateRefBuilder().build(block)
