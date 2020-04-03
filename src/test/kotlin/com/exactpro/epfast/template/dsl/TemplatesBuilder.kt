package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.Template
import com.exactpro.epfast.template.simple.Templates

class TemplatesBuilder internal constructor(val templates: Templates = Templates()) {

    fun templates(block: TemplateBuilder.() -> Unit) {
        val template = Template()
        TemplateBuilder(template).apply(block)
        templates.templates.add(template)
    }
}

fun templates(block: TemplatesBuilder.() -> Unit): Templates =
        TemplatesBuilder(Templates()).apply(block).templates
