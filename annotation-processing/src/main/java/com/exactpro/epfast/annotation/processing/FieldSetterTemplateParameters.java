package com.exactpro.epfast.annotation.processing;

import javax.lang.model.element.Element;
import java.util.List;

class FieldSetterTemplateParameters {
    private Element fastType;

    private TypeName typeName;

    private List<FastFieldElement> fastFields;

    public FieldSetterTemplateParameters(TypeName typeName, List<FastFieldElement> fastFields, Element fastType) {
        this.typeName = typeName;
        this.fastFields = fastFields;
        this.fastType = fastType;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public List<FastFieldElement> getFastFields() {
        return fastFields;
    }

    public Element getFastType() {
        return fastType;
    }
}
