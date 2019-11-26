package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastType;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class FastTypeElement {
    private String fastTypeName;

    private Element fastTypeElement;

    private List<FastFieldElement> fastFields = new ArrayList<>();

    private TypeName fieldSetterTypeName;

    public FastTypeElement(Element fastTypeElement) {
        this.fastTypeName = getNameFromElement(fastTypeElement);
        this.fastTypeElement = fastTypeElement;
    }

    private String getNameFromElement(Element fastTypeElement) {
        String fastType = fastTypeElement.getAnnotation(FastType.class).name();
        if (fastType.isEmpty()) {
            return fastTypeElement.getSimpleName().toString();
        } else {
            return fastType;
        }
    }

    public void addFastField(FastFieldElement fastField) {
        fastFields.add(fastField);
    }

    public String getTypeName() {
        return fastTypeName;
    }

    public Element getElement() {
        return fastTypeElement;
    }

    public List<FastFieldElement> getFastFields() {
        return fastFields;
    }

    public TypeName getFieldSetterTypeName() {
        return fieldSetterTypeName;
    }

    public void setFieldSetterTypeName(TypeName fieldSetterTypeName) {
        this.fieldSetterTypeName = fieldSetterTypeName;
    }
}
