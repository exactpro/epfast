package com.exactpro.epfast.annotation.processing;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.Map;

class MustacheSource {
    private TypeName typeName;

    private Collection<Map.Entry<String, Element>> nameClassEntries;

    MustacheSource(TypeName typeName, Collection<Map.Entry<String, Element>> nameClassEntries) {
        this.typeName = typeName;
        this.nameClassEntries = nameClassEntries;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public Collection<Map.Entry<String, Element>> getNameClassEntries() {
        return nameClassEntries;
    }

}
