/*
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
 */

package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.LengthField

class LengthFieldBuilder internal constructor() {

    internal val field: LengthField = LengthField()

    var name: String by javaProperty(field.fieldId::getName, field.fieldId::setName)

    var namespace: String by javaProperty(field.fieldId::getNamespace, field.fieldId::setNamespace)

    var auxiliaryId: String by javaProperty(field.fieldId::getAuxiliaryId, field.fieldId::setAuxiliaryId)

    fun operator(block: OperatorsBuilder.() -> Unit) {
        field.operator = OperatorsBuilder().apply(block).operator
    }
}
