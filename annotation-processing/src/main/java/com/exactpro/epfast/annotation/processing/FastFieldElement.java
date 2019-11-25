package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class FastFieldElement {
    private Element fastField;

    private String fieldName;

    private TypeMirror parameterType;

    private Element fastType;

    private String methodName;

    public FastFieldElement(Element fastField) {
        this.fastField = fastField;
        this.fieldName = fastField.getAnnotation(FastField.class).name();
        this.parameterType = getParameterTypeFrom(fastField);
        this.fastType = fastField.getEnclosingElement();
        this.methodName = fastField.getSimpleName().toString();
    }

    private TypeMirror getParameterTypeFrom(Element fastField) {
        List<? extends VariableElement> parameters = ((ExecutableElement) fastField).getParameters();
        if (parameters.size() > 1) {
            throw new RuntimeException(
                String.format("Field Setter accepts exactly 1 parameter, found %s", parameters.size())
            );
        }
        return parameters.get(0).asType();
    }

    public Element getFastField() {
        return fastField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public TypeMirror getParameterType() {
        return parameterType;
    }

    public Element getFastType() {
        return fastType;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return "FastFieldElement{" +
            "fastField=" + fastField +
            ", fieldName='" + fieldName + '\'' +
            ", parameterType=" + parameterType +
            ", fastType=" + fastType +
            ", methodName='" + methodName + '\'' +
            '}';
    }
}
