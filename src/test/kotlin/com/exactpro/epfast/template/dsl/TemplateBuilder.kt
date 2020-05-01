/******************************************************************************
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
 ******************************************************************************/

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
