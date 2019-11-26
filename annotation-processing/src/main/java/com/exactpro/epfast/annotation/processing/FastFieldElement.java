package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class FastFieldElement {
    private Element setterMethod;

    private String fieldName;

    public FastFieldElement(Element setterMethod) {
        this.setterMethod = setterMethod;
        this.fieldName = getFastFieldName(setterMethod);
    }

    private String getFastFieldName(Element setterMethod) {
        String fastFieldName = setterMethod.getAnnotation(FastField.class).name();
        if (fastFieldName.isEmpty()) {
            fastFieldName = recoverFastFieldNameFromSetter(getMethodName());
        }
        return fastFieldName;
    }

    static String recoverFastFieldNameFromSetter(String setterMethodName) {
        return setterMethodName.substring(3, 4).toLowerCase() + setterMethodName.substring(4);
    }

    public Element getFastField() {
        return setterMethod;
    }

    public String getFieldName() {
        return fieldName;
    }

    public TypeMirror getParameterType() {
        return ((ExecutableElement) setterMethod).getParameters().get(0).asType();
    }

    public Element getFastType() {
        return setterMethod.getEnclosingElement();
    }

    public String getMethodName() {
        return setterMethod.getSimpleName().toString();
    }

}
