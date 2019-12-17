package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class FastFieldElement {
    private ExecutableElement setterMethod;

    private String fieldName;

    public FastFieldElement(ExecutableElement setterMethod) {
        this.setterMethod = setterMethod;
    }

    private String getFastFieldName(Element setterMethod) {
        String fastFieldName = setterMethod.getAnnotation(FastField.class).name();
        if (fastFieldName.isEmpty()) {
            fastFieldName = getBeanPropertyName(getMethodName());
        }
        return fastFieldName;
    }

    static String getBeanPropertyName(String setterMethodName) {
        if (!setterMethodName.startsWith("set") || setterMethodName.length() <= 3) {
            throw new RuntimeException("Method is not valid java bean setter");
        }
        return setterMethodName.substring(3, 4).toLowerCase() + setterMethodName.substring(4);
    }

    public ExecutableElement getFastField() {
        return setterMethod;
    }

    public String getFieldName() {
        if (fieldName == null) {
            fieldName = getFastFieldName(setterMethod);
        }
        return fieldName;
    }

    public TypeMirror getParameterType() {
        return setterMethod.getParameters().get(0).asType();
    }

    public String getMethodName() {
        return setterMethod.getSimpleName().toString();
    }
}
