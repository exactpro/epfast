package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class FastFieldElement {
    private Element setterMethod;

    private String fieldName;

    private TypeMirror parameterType;

    public FastFieldElement(Element setterMethod) {
        this.setterMethod = setterMethod;
        this.fieldName = setterMethod.getAnnotation(FastField.class).name();
        this.parameterType = ((ExecutableElement) setterMethod).getParameters().get(0).asType();
    }

    public Element getFastField() {
        return setterMethod;
    }

    public String getFieldName() {
        return fieldName;
    }

    public TypeMirror getParameterType() {
        return parameterType;
    }

    public Element getFastType() {
        return setterMethod.getEnclosingElement();
    }

    public String getMethodName() {
        return setterMethod.getSimpleName().toString();
    }

}
