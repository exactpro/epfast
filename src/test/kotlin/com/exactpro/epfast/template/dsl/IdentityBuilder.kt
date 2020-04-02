package com.exactpro.epfast.template.dsl

import com.exactpro.epfast.template.simple.Identity

class IdentityBuilder internal constructor(val identity: Identity = Identity()) : ReferenceBuilder(identity) {

    var auxiliaryId: String? by javaProperty(identity::getAuxiliaryId, identity::setAuxiliaryId)
}
