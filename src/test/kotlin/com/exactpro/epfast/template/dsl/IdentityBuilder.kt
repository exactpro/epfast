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

import com.exactpro.epfast.template.simple.Identity
import com.exactpro.epfast.template.simple.Reference

abstract class AbstractIdentityBuilder<T : Reference> internal constructor(
    internal val value: T
) {
    var name: String by javaProperty(value::getName, value::setName)

    var namespace: String by javaProperty(value::getNamespace, value::setNamespace)
}

class ReferenceBuilder internal constructor() : AbstractIdentityBuilder<Reference>(Reference())

class IdentityBuilder internal constructor() : AbstractIdentityBuilder<Identity>(Identity()) {
    var auxiliaryId: String by javaProperty(value::getAuxiliaryId, value::setAuxiliaryId)
}
